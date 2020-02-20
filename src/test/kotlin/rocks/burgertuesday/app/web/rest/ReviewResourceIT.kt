package rocks.burgertuesday.app.web.rest

import javax.persistence.EntityManager
import kotlin.test.assertNotNull
import org.assertj.core.api.Assertions.assertThat
import org.elasticsearch.index.query.QueryBuilders.queryStringQuery
import org.hamcrest.Matchers.hasItem
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.Validator
import rocks.burgertuesday.app.BurgertuesdayApp
import rocks.burgertuesday.app.config.TestSecurityConfiguration
import rocks.burgertuesday.app.domain.Review
import rocks.burgertuesday.app.domain.Visit
import rocks.burgertuesday.app.repository.ReviewRepository
import rocks.burgertuesday.app.repository.search.ReviewSearchRepository
import rocks.burgertuesday.app.service.ReviewService
import rocks.burgertuesday.app.service.mapper.ReviewMapper
import rocks.burgertuesday.app.web.rest.errors.ExceptionTranslator

/**
 * Integration tests for the [ReviewResource] REST controller.
 *
 * @see ReviewResource
 */
@SpringBootTest(classes = [BurgertuesdayApp::class, TestSecurityConfiguration::class])
class ReviewResourceIT {

    @Autowired
    private lateinit var reviewRepository: ReviewRepository

    @Autowired
    private lateinit var reviewMapper: ReviewMapper

    @Autowired
    private lateinit var reviewService: ReviewService

    /**
     * This repository is mocked in the rocks.burgertuesday.app.repository.search test package.
     *
     * @see rocks.burgertuesday.app.repository.search.ReviewSearchRepositoryMockConfiguration
     */
    @Autowired
    private lateinit var mockReviewSearchRepository: ReviewSearchRepository

    @Autowired
    private lateinit var jacksonMessageConverter: MappingJackson2HttpMessageConverter

    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    @Autowired
    private lateinit var exceptionTranslator: ExceptionTranslator

    @Autowired
    private lateinit var em: EntityManager

    @Autowired
    private lateinit var validator: Validator

    private lateinit var restReviewMockMvc: MockMvc

    private lateinit var review: Review

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val reviewResource = ReviewResource(reviewService)
        this.restReviewMockMvc = MockMvcBuilders.standaloneSetup(reviewResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build()
    }

    @BeforeEach
    fun initTest() {
        review = createEntity(em)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createReview() {
        val databaseSizeBeforeCreate = reviewRepository.findAll().size

        // Create the Review
        val reviewDTO = reviewMapper.toDto(review)
        restReviewMockMvc.perform(
            post("/api/reviews")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(reviewDTO))
        ).andExpect(status().isCreated)

        // Validate the Review in the database
        val reviewList = reviewRepository.findAll()
        assertThat(reviewList).hasSize(databaseSizeBeforeCreate + 1)
        val testReview = reviewList[reviewList.size - 1]
        assertThat(testReview.review).isEqualTo(DEFAULT_REVIEW)
        assertThat(testReview.taste).isEqualTo(DEFAULT_TASTE)
        assertThat(testReview.likeness).isEqualTo(DEFAULT_LIKENESS)
        assertThat(testReview.menuDiversity).isEqualTo(DEFAULT_MENU_DIVERSITY)
        assertThat(testReview.service).isEqualTo(DEFAULT_SERVICE)
        assertThat(testReview.priceLevel).isEqualTo(DEFAULT_PRICE_LEVEL)
        assertThat(testReview.recommended).isEqualTo(DEFAULT_RECOMMENDED)

        // Validate the Review in Elasticsearch
    }

    @Test
    @Transactional
    fun createReviewWithExistingId() {
        val databaseSizeBeforeCreate = reviewRepository.findAll().size

        // Create the Review with an existing ID
        review.id = 1L
        val reviewDTO = reviewMapper.toDto(review)

        // An entity with an existing ID cannot be created, so this API call must fail
        restReviewMockMvc.perform(
            post("/api/reviews")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(reviewDTO))
        ).andExpect(status().isBadRequest)

        // Validate the Review in the database
        val reviewList = reviewRepository.findAll()
        assertThat(reviewList).hasSize(databaseSizeBeforeCreate)

        // Validate the Review in Elasticsearch
        verify(mockReviewSearchRepository, times(0)).save(review)
    }

    @Test
    @Transactional
    fun getAllReviews() {
        // Initialize the database
        reviewRepository.saveAndFlush(review)

        // Get all the reviewList
        restReviewMockMvc.perform(get("/api/reviews?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(review.id?.toInt())))
            .andExpect(jsonPath("$.[*].review").value(hasItem(DEFAULT_REVIEW)))
            .andExpect(jsonPath("$.[*].taste").value(hasItem(DEFAULT_TASTE)))
            .andExpect(jsonPath("$.[*].likeness").value(hasItem(DEFAULT_LIKENESS)))
            .andExpect(jsonPath("$.[*].menuDiversity").value(hasItem(DEFAULT_MENU_DIVERSITY)))
            .andExpect(jsonPath("$.[*].service").value(hasItem(DEFAULT_SERVICE)))
            .andExpect(jsonPath("$.[*].priceLevel").value(hasItem(DEFAULT_PRICE_LEVEL)))
            .andExpect(jsonPath("$.[*].recommended").value(hasItem(DEFAULT_RECOMMENDED)))
    }

    @Test
    @Transactional
    fun getReview() {
        // Initialize the database
        reviewRepository.saveAndFlush(review)

        val id = review.id
        assertNotNull(id)

        // Get the review
        restReviewMockMvc.perform(get("/api/reviews/{id}", id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(id.toInt()))
            .andExpect(jsonPath("$.review").value(DEFAULT_REVIEW))
            .andExpect(jsonPath("$.taste").value(DEFAULT_TASTE))
            .andExpect(jsonPath("$.likeness").value(DEFAULT_LIKENESS))
            .andExpect(jsonPath("$.menuDiversity").value(DEFAULT_MENU_DIVERSITY))
            .andExpect(jsonPath("$.service").value(DEFAULT_SERVICE))
            .andExpect(jsonPath("$.priceLevel").value(DEFAULT_PRICE_LEVEL))
            .andExpect(jsonPath("$.recommended").value(DEFAULT_RECOMMENDED))
    }

    @Test
    @Transactional
    fun getNonExistingReview() {
        // Get the review
        restReviewMockMvc.perform(get("/api/reviews/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }
    @Test
    @Transactional
    fun updateReview() {
        // Initialize the database
        reviewRepository.saveAndFlush(review)

        val databaseSizeBeforeUpdate = reviewRepository.findAll().size

        // Update the review
        val id = review.id
        assertNotNull(id)
        val updatedReview = reviewRepository.findById(id).get()
        // Disconnect from session so that the updates on updatedReview are not directly saved in db
        em.detach(updatedReview)
        updatedReview.review = UPDATED_REVIEW
        updatedReview.taste = UPDATED_TASTE
        updatedReview.likeness = UPDATED_LIKENESS
        updatedReview.menuDiversity = UPDATED_MENU_DIVERSITY
        updatedReview.service = UPDATED_SERVICE
        updatedReview.priceLevel = UPDATED_PRICE_LEVEL
        updatedReview.recommended = UPDATED_RECOMMENDED
        val reviewDTO = reviewMapper.toDto(updatedReview)

        restReviewMockMvc.perform(
            put("/api/reviews")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(reviewDTO))
        ).andExpect(status().isOk)

        // Validate the Review in the database
        val reviewList = reviewRepository.findAll()
        assertThat(reviewList).hasSize(databaseSizeBeforeUpdate)
        val testReview = reviewList[reviewList.size - 1]
        assertThat(testReview.review).isEqualTo(UPDATED_REVIEW)
        assertThat(testReview.taste).isEqualTo(UPDATED_TASTE)
        assertThat(testReview.likeness).isEqualTo(UPDATED_LIKENESS)
        assertThat(testReview.menuDiversity).isEqualTo(UPDATED_MENU_DIVERSITY)
        assertThat(testReview.service).isEqualTo(UPDATED_SERVICE)
        assertThat(testReview.priceLevel).isEqualTo(UPDATED_PRICE_LEVEL)
        assertThat(testReview.recommended).isEqualTo(UPDATED_RECOMMENDED)

        // Validate the Review in Elasticsearch
        verify(mockReviewSearchRepository, times(1)).save(testReview)
    }

    @Test
    @Transactional
    fun updateNonExistingReview() {
        val databaseSizeBeforeUpdate = reviewRepository.findAll().size

        // Create the Review
        val reviewDTO = reviewMapper.toDto(review)

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReviewMockMvc.perform(
            put("/api/reviews")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(reviewDTO))
        ).andExpect(status().isBadRequest)

        // Validate the Review in the database
        val reviewList = reviewRepository.findAll()
        assertThat(reviewList).hasSize(databaseSizeBeforeUpdate)

        // Validate the Review in Elasticsearch
        verify(mockReviewSearchRepository, times(0)).save(review)
    }

    @Test
    @Transactional
    fun deleteReview() {
        // Initialize the database
        reviewRepository.saveAndFlush(review)

        val databaseSizeBeforeDelete = reviewRepository.findAll().size

        val id = review.id
        assertNotNull(id)

        // Delete the review
        restReviewMockMvc.perform(
            delete("/api/reviews/{id}", id)
                .accept(APPLICATION_JSON_UTF8)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val reviewList = reviewRepository.findAll()
        assertThat(reviewList).hasSize(databaseSizeBeforeDelete - 1)

        // Validate the Review in Elasticsearch
        verify(mockReviewSearchRepository, times(1)).deleteById(id)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun searchReview() {
        // InitializesearchReview() the database
        reviewRepository.saveAndFlush(review)
        `when`(mockReviewSearchRepository.search(queryStringQuery("id:" + review.id), PageRequest.of(0, 20)))
            .thenReturn(PageImpl(listOf(review), PageRequest.of(0, 1), 1))
        // Search the review
        restReviewMockMvc.perform(get("/api/_search/reviews?query=id:" + review.id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(review.id?.toInt())))
            .andExpect(jsonPath("$.[*].review").value(hasItem(DEFAULT_REVIEW)))
            .andExpect(jsonPath("$.[*].taste").value(hasItem(DEFAULT_TASTE)))
            .andExpect(jsonPath("$.[*].likeness").value(hasItem(DEFAULT_LIKENESS)))
            .andExpect(jsonPath("$.[*].menuDiversity").value(hasItem(DEFAULT_MENU_DIVERSITY)))
            .andExpect(jsonPath("$.[*].service").value(hasItem(DEFAULT_SERVICE)))
            .andExpect(jsonPath("$.[*].priceLevel").value(hasItem(DEFAULT_PRICE_LEVEL)))
            .andExpect(jsonPath("$.[*].recommended").value(hasItem(DEFAULT_RECOMMENDED)))
    }

    companion object {

        private const val DEFAULT_REVIEW = "AAAAAAAAAA"
        private const val UPDATED_REVIEW = "BBBBBBBBBB"

        private const val DEFAULT_TASTE: Int = 1
        private const val UPDATED_TASTE: Int = 2

        private const val DEFAULT_LIKENESS: Int = 1
        private const val UPDATED_LIKENESS: Int = 2

        private const val DEFAULT_MENU_DIVERSITY: Int = 1
        private const val UPDATED_MENU_DIVERSITY: Int = 2

        private const val DEFAULT_SERVICE: Int = 1
        private const val UPDATED_SERVICE: Int = 2

        private const val DEFAULT_PRICE_LEVEL: Int = 1
        private const val UPDATED_PRICE_LEVEL: Int = 2

        private const val DEFAULT_RECOMMENDED: Boolean = false
        private const val UPDATED_RECOMMENDED: Boolean = true

        /**
         * Create an entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createEntity(em: EntityManager): Review {
            val review = Review(
                review = DEFAULT_REVIEW,
                taste = DEFAULT_TASTE,
                likeness = DEFAULT_LIKENESS,
                menuDiversity = DEFAULT_MENU_DIVERSITY,
                service = DEFAULT_SERVICE,
                priceLevel = DEFAULT_PRICE_LEVEL,
                recommended = DEFAULT_RECOMMENDED
            )

            // Add required entity
            val visit: Visit
            if (em.findAll(Visit::class).isEmpty()) {
                visit = VisitResourceIT.createEntity(em)
                em.persist(visit)
                em.flush()
            } else {
                visit = em.findAll(Visit::class).get(0)
            }
            review.visit = visit
            // Add required entity
            val user = UserResourceIT.createEntity(em)
            em.persist(user)
            em.flush()
            review.user = user
            return review
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): Review {
            val review = Review(
                review = UPDATED_REVIEW,
                taste = UPDATED_TASTE,
                likeness = UPDATED_LIKENESS,
                menuDiversity = UPDATED_MENU_DIVERSITY,
                service = UPDATED_SERVICE,
                priceLevel = UPDATED_PRICE_LEVEL,
                recommended = UPDATED_RECOMMENDED
            )

            // Add required entity
            val visit: Visit
            if (em.findAll(Visit::class).isEmpty()) {
                visit = VisitResourceIT.createUpdatedEntity(em)
                em.persist(visit)
                em.flush()
            } else {
                visit = em.findAll(Visit::class).get(0)
            }
            review.visit = visit
            // Add required entity
            val user = UserResourceIT.createEntity(em)
            em.persist(user)
            em.flush()
            review.user = user
            return review
        }
    }
}
