package com.grosslicht.burgertuesday.repository

import com.grosslicht.burgertuesday.domain.Restaurant
import com.grosslicht.burgertuesday.domain.Review
import com.grosslicht.burgertuesday.domain.Visit
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface RestaurantRepository : PagingAndSortingRepository<Restaurant, Long>

@Repository
interface VisitRepository : PagingAndSortingRepository<Visit, Long>

@Repository
interface ReviewRepository : PagingAndSortingRepository<Review, Long> {
    @Query("select count(id) as amount, createdBy, createdByName FROM Review GROUP BY createdBy, createdByName ORDER BY amount DESC")
    fun getReviewRanking(): List<Array<Any>>
}
