package com.grosslicht.burgertuesday.repository

import com.grosslicht.burgertuesday.domain.Restaurant
import com.grosslicht.burgertuesday.domain.Review
import com.grosslicht.burgertuesday.domain.Visit
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface RestaurantRepository : PagingAndSortingRepository<Restaurant, Long>

@Repository
interface VisitRepository : PagingAndSortingRepository<Visit, Long> {
    @Query("SELECT v FROM Visit v LEFT JOIN v.reviews r ON r.createdBy = :user WHERE r.visit IS NULL ORDER BY v.date DESC",
        countQuery = "SELECT v FROM Visit v LEFT JOIN v.reviews r ON r.createdBy = :user WHERE r.visit IS NULL")
    fun findUnreviewedVisits(@Param("user") user: String): List<Visit>
}

@Repository
interface ReviewRepository : PagingAndSortingRepository<Review, Long> {
    @Query("SELECT count(id) as amount, createdBy, createdByName FROM Review GROUP BY createdBy, createdByName ORDER BY amount DESC")
    fun getReviewRanking(): List<Array<Any>>
}
