package rocks.burgertuesday.app.repository

import rocks.burgertuesday.app.domain.Restaurant
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * Spring Data  repository for the [Restaurant] entity.
 */
@Suppress("unused")
@Repository
interface RestaurantRepository : JpaRepository<Restaurant, Long> {

    @Query("select restaurant from Restaurant restaurant where restaurant.user.login = ?#{principal.username}")
    fun findByUserIsCurrentUser(): List<Restaurant>
}
