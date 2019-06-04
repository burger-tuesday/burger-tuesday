package com.grosslicht.burgertuesday.controller

import com.grosslicht.burgertuesday.controller.util.PaginationUtil
import com.grosslicht.burgertuesday.domain.Review
import com.grosslicht.burgertuesday.domain.Visit
import com.grosslicht.burgertuesday.service.VisitService
import org.springframework.data.domain.Pageable
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.security.Principal
import java.time.LocalDate

@RestController
@RequestMapping("/v1")
class VisitController(private val visitService: VisitService) {
    @GetMapping("/visits")
    @CrossOrigin
    fun getAllVisits(pageable: Pageable): ResponseEntity<List<Visit>> {
        val page = visitService.findAll(pageable)
        val headers = PaginationUtil.generatePaginationHttpHeaders(page, "/v1/visits")
        return ResponseEntity.ok().headers(headers).body(page.content)
    }

    @PostMapping("/visit/{restaurantId}/add/{date}")
    @CrossOrigin
    fun add(
        @PathVariable("restaurantId") restaurantId: String,
        @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) date: LocalDate,
        @RequestParam("sponsored") sponsored: Boolean,
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

    @PostMapping("/visit/{id}/review")
    @CrossOrigin
    fun add(
        @PathVariable("id") id: String,
        @RequestBody review: Review,
        authentication: Authentication
    ): ResponseEntity<Review> {
        return ResponseEntity.ok(visitService.addReview(id, review, authentication))
    }
}
