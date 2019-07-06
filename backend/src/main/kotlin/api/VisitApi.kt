package com.grosslicht.burgertuesday.api

import com.grosslicht.burgertuesday.domain.Review
import com.grosslicht.burgertuesday.domain.Visit
import org.springframework.data.domain.Pageable
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import java.security.Principal
import java.time.LocalDate
import javax.validation.Valid
import javax.validation.constraints.NotNull

@Validated
@RequestMapping("/v1")
interface VisitApi {
    @PostMapping("/visits", consumes = ["application/json"])
    fun createVisit(@Valid @RequestBody visit: Visit): ResponseEntity<Visit> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    @DeleteMapping("/visits/{id}")
    fun deleteVisit(@PathVariable("id") id: Long): ResponseEntity<Unit> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    @GetMapping("/visits")
    fun getAllVisits(pageable: Pageable): ResponseEntity<List<Visit>> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    @GetMapping("/visits/unreviewed")
    fun getAllUnreviewedVisits(principal: Principal): ResponseEntity<List<Visit>> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    @GetMapping("/visits/{id}")
    fun getVisit(@PathVariable("id") id: String): ResponseEntity<Visit> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    @GetMapping("/_search/visits")
    fun searchVisits(
        @NotNull @RequestParam(value = "query", required = true) query: String,
        pageable: Pageable
    ): ResponseEntity<List<Visit>> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    @PutMapping("/visits", consumes = ["application/json"])
    fun updateVisit(@Valid @RequestBody visit: Visit): ResponseEntity<Visit> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    @PostMapping("/visits/{restaurantId}/add/{date}")
    fun addVisit(
        @PathVariable("restaurantId") restaurantId: String,
        @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) date: LocalDate,
        @RequestParam("sponsored") sponsored: Boolean,
        principal: Principal
    ): ResponseEntity<Visit> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    @PostMapping("/visits/{id}/review")
    fun addReviewToVisit(
        @PathVariable("id") id: String,
        @RequestBody review: Review,
        authentication: Authentication
    ): ResponseEntity<Review> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }
}
