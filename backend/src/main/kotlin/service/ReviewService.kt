package com.grosslicht.burgertuesday.service

import com.grosslicht.burgertuesday.repository.ReviewRepository
import com.grosslicht.burgertuesday.repository.VisitRepository
import org.springframework.stereotype.Service

@Service
class ReviewService(
    private val visitRepository: VisitRepository,
    private val reviewRepository: ReviewRepository
)
