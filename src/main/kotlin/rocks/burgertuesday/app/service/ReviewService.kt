package rocks.burgertuesday.app.service

import rocks.burgertuesday.app.service.dto.ReviewDTO

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

import java.util.Optional

/**
 * Service Interface for managing [rocks.burgertuesday.app.domain.Review].
 */
interface ReviewService {

    /**
     * Save a review.
     *
     * @param reviewDTO the entity to save.
     * @return the persisted entity.
     */
    fun save(reviewDTO: ReviewDTO): ReviewDTO

    /**
     * Get all the reviews.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    fun findAll(pageable: Pageable): Page<ReviewDTO>

    /**
     * Get the "id" review.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: Long): Optional<ReviewDTO>

    /**
     * Delete the "id" review.
     *
     * @param id the id of the entity.
     */
    fun delete(id: Long)

    /**
     * Search for the review corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    fun search(query: String, pageable: Pageable): Page<ReviewDTO>
}
