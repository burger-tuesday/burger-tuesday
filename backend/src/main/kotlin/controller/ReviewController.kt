package com.grosslicht.burgertuesday.controller

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.grosslicht.burgertuesday.api.ReviewApi
import com.grosslicht.burgertuesday.domain.Review
import com.grosslicht.burgertuesday.domain.ReviewEvent
import com.grosslicht.burgertuesday.domain.ToplistEntry
import com.grosslicht.burgertuesday.service.ReviewService
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.ServerSentEvent
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.DirectProcessor
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink
import java.time.Duration

@RestController
class ReviewController(private val reviewService: ReviewService) : ReviewApi {
    companion object {
        private val mapper = jacksonObjectMapper()
            .registerModule(JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)

        private val processor = DirectProcessor.create<Review>().serialize()
        private val flux = processor.map {
            ServerSentEvent.builder(
                mapper.writeValueAsString(
                    ReviewEvent(
                        it.visit!!.date,
                        it.visit!!.restaurant.name,
                        it.createdByName!!.split(" ").map(String::first).joinToString(""),
                        it.recommended
                    )
                )
            ).event("review").build()
        }
        val sink = processor.sink(FluxSink.OverflowStrategy.LATEST)
    }

    override fun liveReviews(): Flux<ServerSentEvent<String>> {
        val pingFlux = Flux.interval(Duration.ofSeconds(10))
            .map {
                ServerSentEvent.builder(System.currentTimeMillis().toString()).event("ping").build()
            }
        return Flux.merge(flux, pingFlux)
    }

    override fun getReviewRanking(): ResponseEntity<List<ToplistEntry>> {
        return ResponseEntity.ok(reviewService.getReviewRanking())
    }
}
