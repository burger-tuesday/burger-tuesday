package rocks.burgertuesday.app.service

import rocks.burgertuesday.app.service.dto.RestaurantDTO

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

import java.util.Optional

/**
 * Service Interface for managing [rocks.burgertuesday.app.domain.Restaurant].
 */
interface RestaurantService {

    /**
     * Save a restaurant.
     *
     * @param restaurantDTO the entity to save.
     * @return the persisted entity.
     */
    fun save(restaurantDTO: RestaurantDTO): RestaurantDTO

    /**
     * Get all the restaurants.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    fun findAll(pageable: Pageable): Page<RestaurantDTO>

    /**
     * Get the "id" restaurant.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: Long): Optional<RestaurantDTO>

    /**
     * Delete the "id" restaurant.
     *
     * @param id the id of the entity.
     */
    fun delete(id: Long)

    /**
     * Search for the restaurant corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    fun search(query: String, pageable: Pageable): Page<RestaurantDTO>
}
