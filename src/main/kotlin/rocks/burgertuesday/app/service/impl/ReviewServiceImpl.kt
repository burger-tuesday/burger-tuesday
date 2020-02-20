package rocks.burgertuesday.app.service.impl

import java.util.Optional
import org.elasticsearch.index.query.QueryBuilders.queryStringQuery
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import rocks.burgertuesday.app.domain.Review
import rocks.burgertuesday.app.repository.ReviewRepository
import rocks.burgertuesday.app.repository.search.ReviewSearchRepository
import rocks.burgertuesday.app.service.ReviewService
import rocks.burgertuesday.app.service.dto.ReviewDTO
import rocks.burgertuesday.app.service.mapper.ReviewMapper

/**
 * Service Implementation for managing [Review].
 */
@Service
@Transactional
class ReviewServiceImpl(
    private val reviewRepository: ReviewRepository,
    private val reviewMapper: ReviewMapper,
    private val reviewSearchRepository: ReviewSearchRepository
) : ReviewService {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Save a review.
     *
     * @param reviewDTO the entity to save.
     * @return the persisted entity.
     */
    override fun save(reviewDTO: ReviewDTO): ReviewDTO {
        log.debug("Request to save Review : {}", reviewDTO)

        var review = reviewMapper.toEntity(reviewDTO)
        review = reviewRepository.save(review)
        val result = reviewMapper.toDto(review)
        reviewSearchRepository.save(review)
        return result
    }

    /**
     * Get all the reviews.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable): Page<ReviewDTO> {
        log.debug("Request to get all Reviews")
        return reviewRepository.findAll(pageable)
            .map(reviewMapper::toDto)
    }

    /**
     * Get one review by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    override fun findOne(id: Long): Optional<ReviewDTO> {
        log.debug("Request to get Review : {}", id)
        return reviewRepository.findById(id)
            .map(reviewMapper::toDto)
    }

    /**
     * Delete the review by id.
     *
     * @param id the id of the entity.
     */
    override fun delete(id: Long) {
        log.debug("Request to delete Review : {}", id)

        reviewRepository.deleteById(id)
        reviewSearchRepository.deleteById(id)
    }

    /**
     * Search for the review corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    override fun search(query: String, pageable: Pageable): Page<ReviewDTO> {
        log.debug("Request to search for a page of Reviews for query {}", query)
        return reviewSearchRepository.search(queryStringQuery(query), pageable).map(reviewMapper::toDto)
    }
}
