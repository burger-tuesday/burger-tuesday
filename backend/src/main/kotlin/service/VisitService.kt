package com.grosslicht.burgertuesday.service

import at.favre.lib.idmask.IdMask
import com.grosslicht.burgertuesday.api.auth0.Auth0Api
import com.grosslicht.burgertuesday.controller.ReviewController
import com.grosslicht.burgertuesday.domain.Review
import com.grosslicht.burgertuesday.domain.Visit
import com.grosslicht.burgertuesday.repository.RestaurantRepository
import com.grosslicht.burgertuesday.repository.ReviewRepository
import com.grosslicht.burgertuesday.repository.VisitRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate

@Service
class VisitService(
    private val restaurantRepository: RestaurantRepository,
    private val visitRepository: VisitRepository,
    private val reviewRepository: ReviewRepository,
    private val idMask: IdMask<Long>,
    private val auth0Api: Auth0Api
) {
    @PreAuthorize("hasAuthority('manage:visits')")
    fun addVisit(
        restaurantId: String,
        date: LocalDate,
        sponsored: Boolean,
        username: String
    ): Visit {
        val unmaskedId = idMask.unmask(restaurantId)
        val restaurant = restaurantRepository.findById(unmaskedId)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found") }
        val visit = visitRepository.save(
            Visit(
                date = date,
                restaurant = restaurant,
                sponsored = sponsored,
                createdBy = username
            )
        )
        restaurant.addVisit(visit)
        restaurantRepository.save(restaurant)
        return visit
    }

    fun addReview(
        id: String,
        review: Review,
        authentication: Authentication
    ): Review {
        val unmaskedId = idMask.unmask(id)
        val visit = visitRepository.findById(unmaskedId)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Visit not found") }
        if (visit.reviews.any { r -> r.createdBy == authentication.principal }) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "User already reviewed this visit.")
        }
        val userInfo = auth0Api.userInfo("Bearer ${authentication.credentials}").execute().body()
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while resolving user")
        review.createdBy = authentication.principal.toString()
        review.createdByName = userInfo.name
        review.visit = visit
        val savedReview = reviewRepository.save(review)
        visit.addReview(savedReview)
        visitRepository.save(visit)
        ReviewController.sink.next(savedReview)
        return savedReview
    }

    fun findAll(pageable: Pageable): Page<Visit> {
        return visitRepository.findAll(pageable)
    }

    fun findAllUnreviewed(user: String): List<Visit> {
        return visitRepository.findUnreviewedVisits(user)
    }
}
