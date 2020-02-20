package rocks.burgertuesday.app.repository

import org.springframework.data.jpa.repository.JpaRepository
import rocks.burgertuesday.app.domain.Authority

/**
 * Spring Data JPA repository for the [Authority] entity.
 */

interface AuthorityRepository : JpaRepository<Authority, String>
