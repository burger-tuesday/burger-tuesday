package rocks.burgertuesday.app.repository

import rocks.burgertuesday.app.domain.Review
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * Spring Data  repository for the [Review] entity.
 */
@Suppress("unused")
@Repository
interface ReviewRepository : JpaRepository<Review, Long> {

    @Query("select review from Review review where review.user.login = ?#{principal.username}")
    fun findByUserIsCurrentUser(): List<Review>
}
