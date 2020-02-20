package rocks.burgertuesday.app.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import rocks.burgertuesday.app.domain.Visit

/**
 * Spring Data  repository for the [Visit] entity.
 */
@Suppress("unused")
@Repository
interface VisitRepository : JpaRepository<Visit, Long> {

    @Query("select visit from Visit visit where visit.user.login = ?#{principal.username}")
    fun findByUserIsCurrentUser(): List<Visit>

    @Query("SELECT v FROM Visit v LEFT JOIN v.reviews r ON r.user.login = ?#{principal.username} WHERE r.visit IS NULL ORDER BY v.date DESC",
        countQuery = "SELECT v FROM Visit v LEFT JOIN v.reviews r ON r.user.login = ?#{principal.username} WHERE r.visit IS NULL")
    fun findUnreviewedVisits(): List<Visit>
}
