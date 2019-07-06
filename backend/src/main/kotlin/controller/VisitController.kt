package com.grosslicht.burgertuesday.controller

import com.grosslicht.burgertuesday.api.VisitApi
import com.grosslicht.burgertuesday.controller.util.PaginationUtil
import com.grosslicht.burgertuesday.domain.Review
import com.grosslicht.burgertuesday.domain.Visit
import com.grosslicht.burgertuesday.service.VisitService
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.RestController
import java.security.Principal
import java.time.LocalDate

@RestController
class VisitController(private val visitService: VisitService) : VisitApi {
    override fun getAllVisits(pageable: Pageable): ResponseEntity<List<Visit>> {
        val page = visitService.findAll(pageable)
        val headers = PaginationUtil.generatePaginationHttpHeaders(page, "/v1/visits")
        return ResponseEntity.ok().headers(headers).body(page.content)
    }

    override fun getAllUnreviewedVisits(principal: Principal): ResponseEntity<List<Visit>> {
        return ResponseEntity.ok(visitService.findAllUnreviewed(principal.name))
    }

    override fun addVisit(
        restaurantId: String,
        date: LocalDate,
        sponsored: Boolean,
        principal: Principal
    ): ResponseEntity<Visit> {
        return ResponseEntity.ok(
            visitService.addVisit(
                restaurantId,
                date,
                sponsored,
                principal.name
            )
        )
    }

    override fun addReviewToVisit(
        id: String,
        review: Review,
        authentication: Authentication
    ): ResponseEntity<Review> {
        return ResponseEntity.ok(visitService.addReview(id, review, authentication))
    }
}
