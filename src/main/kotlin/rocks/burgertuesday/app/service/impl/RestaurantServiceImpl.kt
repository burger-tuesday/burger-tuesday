package rocks.burgertuesday.app.service.impl

import java.util.Optional
import org.elasticsearch.index.query.QueryBuilders.queryStringQuery
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import rocks.burgertuesday.app.domain.Restaurant
import rocks.burgertuesday.app.repository.RestaurantRepository
import rocks.burgertuesday.app.repository.search.RestaurantSearchRepository
import rocks.burgertuesday.app.service.RestaurantService
import rocks.burgertuesday.app.service.dto.RestaurantDTO
import rocks.burgertuesday.app.service.mapper.RestaurantMapper

/**
 * Service Implementation for managing [Restaurant].
 */
@Service
@Transactional
class RestaurantServiceImpl(
    private val restaurantRepository: RestaurantRepository,
    private val restaurantMapper: RestaurantMapper,
    private val restaurantSearchRepository: RestaurantSearchRepository
) : RestaurantService {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Save a restaurant.
     *
     * @param restaurantDTO the entity to save.
     * @return the persisted entity.
     */
    override fun save(restaurantDTO: RestaurantDTO): RestaurantDTO {
        log.debug("Request to save Restaurant : {}", restaurantDTO)

        var restaurant = restaurantMapper.toEntity(restaurantDTO)
        restaurant = restaurantRepository.save(restaurant)
        val result = restaurantMapper.toDto(restaurant)
        restaurantSearchRepository.save(restaurant)
        return result
    }

    /**
     * Get all the restaurants.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable): Page<RestaurantDTO> {
        log.debug("Request to get all Restaurants")
        return restaurantRepository.findAll(pageable)
            .map(restaurantMapper::toDto)
    }

    /**
     * Get one restaurant by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    override fun findOne(id: Long): Optional<RestaurantDTO> {
        log.debug("Request to get Restaurant : {}", id)
        return restaurantRepository.findById(id)
            .map(restaurantMapper::toDto)
    }

    /**
     * Delete the restaurant by id.
     *
     * @param id the id of the entity.
     */
    override fun delete(id: Long) {
        log.debug("Request to delete Restaurant : {}", id)

        restaurantRepository.deleteById(id)
        restaurantSearchRepository.deleteById(id)
    }

    /**
     * Search for the restaurant corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    override fun search(query: String, pageable: Pageable): Page<RestaurantDTO> {
        log.debug("Request to search for a page of Restaurants for query {}", query)
        return restaurantSearchRepository.search(queryStringQuery(query), pageable).map(restaurantMapper::toDto)
    }
}
