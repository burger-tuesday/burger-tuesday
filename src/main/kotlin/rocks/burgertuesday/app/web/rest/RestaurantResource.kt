package rocks.burgertuesday.app.web.rest

import rocks.burgertuesday.app.service.RestaurantService
import rocks.burgertuesday.app.web.rest.errors.BadRequestAlertException
import rocks.burgertuesday.app.service.dto.RestaurantDTO

import io.github.jhipster.web.util.HeaderUtil
import io.github.jhipster.web.util.PaginationUtil
import io.github.jhipster.web.util.ResponseUtil
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Pageable
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import rocks.burgertuesday.app.security.ADMIN

import javax.validation.Valid
import java.net.URI
import java.net.URISyntaxException

private const val ENTITY_NAME = "restaurant"

/**
 * REST controller for managing [rocks.burgertuesday.app.domain.Restaurant].
 */
@RestController
@RequestMapping("/api")
class RestaurantResource(
    private val restaurantService: RestaurantService
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /restaurants` : Create a new restaurant.
     *
     * @param restaurantDTO the restaurantDTO to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new restaurantDTO, or with status `400 (Bad Request)` if the restaurant has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/restaurants")
    @PreAuthorize("hasRole(\"$ADMIN\")")
    fun createRestaurant(@Valid @RequestBody restaurantDTO: RestaurantDTO): ResponseEntity<RestaurantDTO> {
        log.debug("REST request to save Restaurant : {}", restaurantDTO)
        if (restaurantDTO.id != null) {
            throw BadRequestAlertException(
                "A new restaurant cannot already have an ID",
                ENTITY_NAME, "idexists"
            )
        }
        val result = restaurantService.save(restaurantDTO)
        return ResponseEntity.created(URI("/api/restaurants/" + result.id))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * `PUT  /restaurants` : Updates an existing restaurant.
     *
     * @param restaurantDTO the restaurantDTO to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated restaurantDTO,
     * or with status `400 (Bad Request)` if the restaurantDTO is not valid,
     * or with status `500 (Internal Server Error)` if the restaurantDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/restaurants")
    @PreAuthorize("hasRole(\"$ADMIN\")")
    fun updateRestaurant(@Valid @RequestBody restaurantDTO: RestaurantDTO): ResponseEntity<RestaurantDTO> {
        log.debug("REST request to update Restaurant : {}", restaurantDTO)
        if (restaurantDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        val result = restaurantService.save(restaurantDTO)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName, true, ENTITY_NAME, restaurantDTO.id.toString()
                )
            )
            .body(result)
    }

    /**
     * `GET  /restaurants` : get all the restaurants.
     *

     * @param pageable the pagination information.

     * @return the [ResponseEntity] with status `200 (OK)` and the list of restaurants in body.
     */
    @GetMapping("/restaurants")
    fun getAllRestaurants(
        pageable: Pageable
    ): ResponseEntity<MutableList<RestaurantDTO>> {
        log.debug("REST request to get a page of Restaurants")
        val page = restaurantService.findAll(pageable)
        val headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }

    /**
     * `GET  /restaurants/:id` : get the "id" restaurant.
     *
     * @param id the id of the restaurantDTO to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the restaurantDTO, or with status `404 (Not Found)`.
     */
    @GetMapping("/restaurants/{id}")
    fun getRestaurant(@PathVariable id: Long): ResponseEntity<RestaurantDTO> {
        log.debug("REST request to get Restaurant : {}", id)
        val restaurantDTO = restaurantService.findOne(id)
        return ResponseUtil.wrapOrNotFound(restaurantDTO)
    }

    /**
     *  `DELETE  /restaurants/:id` : delete the "id" restaurant.
     *
     * @param id the id of the restaurantDTO to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/restaurants/{id}")
    @PreAuthorize("hasRole(\"$ADMIN\")")
    fun deleteRestaurant(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete Restaurant : {}", id)
        restaurantService.delete(id)
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }

    /**
     * `SEARCH  /_search/restaurants?query=:query` : search for the restaurant corresponding
     * to the query.
     *
     * @param query the query of the restaurant search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/restaurants")
    fun searchRestaurants(@RequestParam query: String, pageable: Pageable): ResponseEntity<MutableList<RestaurantDTO>> {
        log.debug("REST request to search for a page of Restaurants for query {}", query)
        val page = restaurantService.search(query, pageable)
        val headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }
}
