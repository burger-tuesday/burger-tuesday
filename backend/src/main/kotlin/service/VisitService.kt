package com.grosslicht.burgertuesday.service

import com.grosslicht.burgertuesday.domain.Visit
import com.grosslicht.burgertuesday.repository.RestaurantRepository
import com.grosslicht.burgertuesday.repository.VisitRepository
import org.springframework.data.jpa.domain.AbstractAuditable_.createdBy
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate

@Service
class VisitService(
    private val restaurantRepository: RestaurantRepository,
    private val visitRepository: VisitRepository
) {
    @PreAuthorize("hasAuthority('manage:visits')")
    fun addVisit(placeId: Long, date: LocalDate, sponsored: Boolean, username: String): Visit {
        val restaurant = restaurantRepository.findById(placeId)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found") }
        val visit = visitRepository.save(Visit(date = date, restaurant = restaurant, sponsored = sponsored, createdBy = username))
        restaurant.addVisit(visit)
        restaurantRepository.save(restaurant)
        return visit
    }
}
