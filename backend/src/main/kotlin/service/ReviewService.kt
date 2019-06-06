package com.grosslicht.burgertuesday.service

import com.grosslicht.burgertuesday.domain.ToplistEntry
import com.grosslicht.burgertuesday.repository.ReviewRepository
import org.springframework.stereotype.Service

@Service
class ReviewService(private val reviewRepository: ReviewRepository) {
    fun getReviewRanking(): List<ToplistEntry> {
        val reviewRankings = reviewRepository.getReviewRanking()
            .map { Triple(it[0] as Long, it[1] as String, it[2] as String) }
        val reviewAmounts = reviewRankings.mapTo(sortedSetOf(reverseOrder()), { it.first })
        return reviewRankings.map {
            ToplistEntry(
                reviewAmounts.indexOf(it.first) + 1,
                it.first,
                it.second,
                it.third
            )
        }
    }
}
