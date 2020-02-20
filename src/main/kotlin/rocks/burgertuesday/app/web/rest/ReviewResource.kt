package rocks.burgertuesday.app.web.rest

import rocks.burgertuesday.app.service.ReviewService
import rocks.burgertuesday.app.web.rest.errors.BadRequestAlertException
import rocks.burgertuesday.app.service.dto.ReviewDTO

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

private const val ENTITY_NAME = "review"

/**
 * REST controller for managing [rocks.burgertuesday.app.domain.Review].
 */
@RestController
@RequestMapping("/api")
class ReviewResource(
    private val reviewService: ReviewService
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /reviews` : Create a new review.
     *
     * @param reviewDTO the reviewDTO to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new reviewDTO, or with status `400 (Bad Request)` if the review has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/reviews")
    @PreAuthorize("hasRole(\"$ADMIN\")")
    fun createReview(@Valid @RequestBody reviewDTO: ReviewDTO): ResponseEntity<ReviewDTO> {
        log.debug("REST request to save Review : {}", reviewDTO)
        if (reviewDTO.id != null) {
            throw BadRequestAlertException(
                "A new review cannot already have an ID",
                ENTITY_NAME, "idexists"
            )
        }
        val result = reviewService.save(reviewDTO)
        return ResponseEntity.created(URI("/api/reviews/" + result.id))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * `PUT  /reviews` : Updates an existing review.
     *
     * @param reviewDTO the reviewDTO to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated reviewDTO,
     * or with status `400 (Bad Request)` if the reviewDTO is not valid,
     * or with status `500 (Internal Server Error)` if the reviewDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/reviews")
    @PreAuthorize("hasRole(\"$ADMIN\")")
    fun updateReview(@Valid @RequestBody reviewDTO: ReviewDTO): ResponseEntity<ReviewDTO> {
        log.debug("REST request to update Review : {}", reviewDTO)
        if (reviewDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        val result = reviewService.save(reviewDTO)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName, true, ENTITY_NAME, reviewDTO.id.toString()
                )
            )
            .body(result)
    }

    /**
     * `GET  /reviews` : get all the reviews.
     *

     * @param pageable the pagination information.

     * @return the [ResponseEntity] with status `200 (OK)` and the list of reviews in body.
     */
    @GetMapping("/reviews")
    fun getAllReviews(
        pageable: Pageable
    ): ResponseEntity<MutableList<ReviewDTO>> {
        log.debug("REST request to get a page of Reviews")
        val page = reviewService.findAll(pageable)
        val headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }

    /**
     * `GET  /reviews/:id` : get the "id" review.
     *
     * @param id the id of the reviewDTO to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the reviewDTO, or with status `404 (Not Found)`.
     */
    @GetMapping("/reviews/{id}")
    fun getReview(@PathVariable id: Long): ResponseEntity<ReviewDTO> {
        log.debug("REST request to get Review : {}", id)
        val reviewDTO = reviewService.findOne(id)
        return ResponseUtil.wrapOrNotFound(reviewDTO)
    }

    /**
     *  `DELETE  /reviews/:id` : delete the "id" review.
     *
     * @param id the id of the reviewDTO to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/reviews/{id}")
    @PreAuthorize("hasRole(\"$ADMIN\")")
    fun deleteReview(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete Review : {}", id)
        reviewService.delete(id)
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }

    /**
     * `SEARCH  /_search/reviews?query=:query` : search for the review corresponding
     * to the query.
     *
     * @param query the query of the review search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/reviews")
    fun searchReviews(@RequestParam query: String, pageable: Pageable): ResponseEntity<MutableList<ReviewDTO>> {
        log.debug("REST request to search for a page of Reviews for query {}", query)
        val page = reviewService.search(query, pageable)
        val headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }
}
