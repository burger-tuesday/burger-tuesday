package com.grosslicht.burgertuesday.api

import com.grosslicht.burgertuesday.domain.Review
import com.grosslicht.burgertuesday.domain.ToplistEntry
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.ServerSentEvent
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import reactor.core.publisher.Flux
import javax.validation.Valid
import javax.validation.constraints.NotNull

@Validated
@RequestMapping("/v1")
interface ReviewApi {
    @PostMapping("/reviews", consumes = ["application/json"])
    fun createReview(@Valid @RequestBody review: Review): ResponseEntity<Review> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    @DeleteMapping("/reviews/{id}")
    fun deleteReview(@PathVariable("id") id: String): ResponseEntity<Unit> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    @GetMapping("/reviews")
    fun getAllReviews(pageable: Pageable): ResponseEntity<List<Review>> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    @GetMapping("/reviews/{id}")
    fun getReview(@PathVariable("id") id: String): ResponseEntity<Review> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    @GetMapping("/_search/reviews")
    fun searchReviews(
        @NotNull @RequestParam(value = "query", required = true) query: String,
        pageable: Pageable
    ): ResponseEntity<List<Review>> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    @PutMapping("/reviews", consumes = ["application/json"])
    fun updateReview(@Valid @RequestBody review: Review): ResponseEntity<Review> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    @GetMapping(path = ["/reviews/live"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun liveReviews(): Flux<ServerSentEvent<String>> {
        return Flux.error<ServerSentEvent<String>>(NotImplementedError())
    }

    @GetMapping("/reviews/top")
    fun getReviewRanking(): ResponseEntity<List<ToplistEntry>> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }
}
