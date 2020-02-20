package rocks.burgertuesday.app.web.rest

import java.math.BigDecimal
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
import rocks.burgertuesday.app.domain.Restaurant
import rocks.burgertuesday.app.repository.RestaurantRepository
import rocks.burgertuesday.app.repository.search.RestaurantSearchRepository
import rocks.burgertuesday.app.service.RestaurantService
import rocks.burgertuesday.app.service.mapper.RestaurantMapper
import rocks.burgertuesday.app.web.rest.errors.ExceptionTranslator

/**
 * Integration tests for the [RestaurantResource] REST controller.
 *
 * @see RestaurantResource
 */
@SpringBootTest(classes = [BurgertuesdayApp::class, TestSecurityConfiguration::class])
class RestaurantResourceIT {

    @Autowired
    private lateinit var restaurantRepository: RestaurantRepository

    @Autowired
    private lateinit var restaurantMapper: RestaurantMapper

    @Autowired
    private lateinit var restaurantService: RestaurantService

    /**
     * This repository is mocked in the rocks.burgertuesday.app.repository.search test package.
     *
     * @see rocks.burgertuesday.app.repository.search.RestaurantSearchRepositoryMockConfiguration
     */
    @Autowired
    private lateinit var mockRestaurantSearchRepository: RestaurantSearchRepository

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

    private lateinit var restRestaurantMockMvc: MockMvc

    private lateinit var restaurant: Restaurant

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val restaurantResource = RestaurantResource(restaurantService)
        this.restRestaurantMockMvc = MockMvcBuilders.standaloneSetup(restaurantResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build()
    }

    @BeforeEach
    fun initTest() {
        restaurant = createEntity(em)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createRestaurant() {
        val databaseSizeBeforeCreate = restaurantRepository.findAll().size

        // Create the Restaurant
        val restaurantDTO = restaurantMapper.toDto(restaurant)
        restRestaurantMockMvc.perform(
            post("/api/restaurants")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(restaurantDTO))
        ).andExpect(status().isCreated)

        // Validate the Restaurant in the database
        val restaurantList = restaurantRepository.findAll()
        assertThat(restaurantList).hasSize(databaseSizeBeforeCreate + 1)
        val testRestaurant = restaurantList[restaurantList.size - 1]
        assertThat(testRestaurant.placeId).isEqualTo(DEFAULT_PLACE_ID)
        assertThat(testRestaurant.name).isEqualTo(DEFAULT_NAME)
        assertThat(testRestaurant.address).isEqualTo(DEFAULT_ADDRESS)
        assertThat(testRestaurant.vicinity).isEqualTo(DEFAULT_VICINITY)
        assertThat(testRestaurant.url).isEqualTo(DEFAULT_URL)
        assertThat(testRestaurant.website).isEqualTo(DEFAULT_WEBSITE)
        assertThat(testRestaurant.googleRating).isEqualTo(DEFAULT_GOOGLE_RATING)
        assertThat(testRestaurant.btRating).isEqualTo(DEFAULT_BT_RATING)
        assertThat(testRestaurant.numberOfReviews).isEqualTo(DEFAULT_NUMBER_OF_REVIEWS)
        assertThat(testRestaurant.priceLevel).isEqualTo(DEFAULT_PRICE_LEVEL)
        assertThat(testRestaurant.permanentlyClosed).isEqualTo(DEFAULT_PERMANENTLY_CLOSED)

        // Validate the Restaurant in Elasticsearch
    }

    @Test
    @Transactional
    fun createRestaurantWithExistingId() {
        val databaseSizeBeforeCreate = restaurantRepository.findAll().size

        // Create the Restaurant with an existing ID
        restaurant.id = 1L
        val restaurantDTO = restaurantMapper.toDto(restaurant)

        // An entity with an existing ID cannot be created, so this API call must fail
        restRestaurantMockMvc.perform(
            post("/api/restaurants")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(restaurantDTO))
        ).andExpect(status().isBadRequest)

        // Validate the Restaurant in the database
        val restaurantList = restaurantRepository.findAll()
        assertThat(restaurantList).hasSize(databaseSizeBeforeCreate)

        // Validate the Restaurant in Elasticsearch
        verify(mockRestaurantSearchRepository, times(0)).save(restaurant)
    }

    @Test
    @Transactional
    fun getAllRestaurants() {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant)

        // Get all the restaurantList
        restRestaurantMockMvc.perform(get("/api/restaurants?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(restaurant.id?.toInt())))
            .andExpect(jsonPath("$.[*].placeId").value(hasItem(DEFAULT_PLACE_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].vicinity").value(hasItem(DEFAULT_VICINITY)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE)))
            .andExpect(jsonPath("$.[*].googleRating").value(hasItem(DEFAULT_GOOGLE_RATING.toInt())))
            .andExpect(jsonPath("$.[*].btRating").value(hasItem(DEFAULT_BT_RATING.toInt())))
            .andExpect(jsonPath("$.[*].numberOfReviews").value(hasItem(DEFAULT_NUMBER_OF_REVIEWS)))
            .andExpect(jsonPath("$.[*].priceLevel").value(hasItem(DEFAULT_PRICE_LEVEL)))
            .andExpect(jsonPath("$.[*].permanentlyClosed").value(hasItem(DEFAULT_PERMANENTLY_CLOSED)))
    }

    @Test
    @Transactional
    fun getRestaurant() {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant)

        val id = restaurant.id
        assertNotNull(id)

        // Get the restaurant
        restRestaurantMockMvc.perform(get("/api/restaurants/{id}", id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(id.toInt()))
            .andExpect(jsonPath("$.placeId").value(DEFAULT_PLACE_ID))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.vicinity").value(DEFAULT_VICINITY))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.website").value(DEFAULT_WEBSITE))
            .andExpect(jsonPath("$.googleRating").value(DEFAULT_GOOGLE_RATING.toInt()))
            .andExpect(jsonPath("$.btRating").value(DEFAULT_BT_RATING.toInt()))
            .andExpect(jsonPath("$.numberOfReviews").value(DEFAULT_NUMBER_OF_REVIEWS))
            .andExpect(jsonPath("$.priceLevel").value(DEFAULT_PRICE_LEVEL))
            .andExpect(jsonPath("$.permanentlyClosed").value(DEFAULT_PERMANENTLY_CLOSED))
    }

    @Test
    @Transactional
    fun getNonExistingRestaurant() {
        // Get the restaurant
        restRestaurantMockMvc.perform(get("/api/restaurants/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }
    @Test
    @Transactional
    fun updateRestaurant() {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant)

        val databaseSizeBeforeUpdate = restaurantRepository.findAll().size

        // Update the restaurant
        val id = restaurant.id
        assertNotNull(id)
        val updatedRestaurant = restaurantRepository.findById(id).get()
        // Disconnect from session so that the updates on updatedRestaurant are not directly saved in db
        em.detach(updatedRestaurant)
        updatedRestaurant.placeId = UPDATED_PLACE_ID
        updatedRestaurant.name = UPDATED_NAME
        updatedRestaurant.address = UPDATED_ADDRESS
        updatedRestaurant.vicinity = UPDATED_VICINITY
        updatedRestaurant.url = UPDATED_URL
        updatedRestaurant.website = UPDATED_WEBSITE
        updatedRestaurant.googleRating = UPDATED_GOOGLE_RATING
        updatedRestaurant.btRating = UPDATED_BT_RATING
        updatedRestaurant.numberOfReviews = UPDATED_NUMBER_OF_REVIEWS
        updatedRestaurant.priceLevel = UPDATED_PRICE_LEVEL
        updatedRestaurant.permanentlyClosed = UPDATED_PERMANENTLY_CLOSED
        val restaurantDTO = restaurantMapper.toDto(updatedRestaurant)

        restRestaurantMockMvc.perform(
            put("/api/restaurants")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(restaurantDTO))
        ).andExpect(status().isOk)

        // Validate the Restaurant in the database
        val restaurantList = restaurantRepository.findAll()
        assertThat(restaurantList).hasSize(databaseSizeBeforeUpdate)
        val testRestaurant = restaurantList[restaurantList.size - 1]
        assertThat(testRestaurant.placeId).isEqualTo(UPDATED_PLACE_ID)
        assertThat(testRestaurant.name).isEqualTo(UPDATED_NAME)
        assertThat(testRestaurant.address).isEqualTo(UPDATED_ADDRESS)
        assertThat(testRestaurant.vicinity).isEqualTo(UPDATED_VICINITY)
        assertThat(testRestaurant.url).isEqualTo(UPDATED_URL)
        assertThat(testRestaurant.website).isEqualTo(UPDATED_WEBSITE)
        assertThat(testRestaurant.googleRating).isEqualTo(UPDATED_GOOGLE_RATING)
        assertThat(testRestaurant.btRating).isEqualTo(UPDATED_BT_RATING)
        assertThat(testRestaurant.numberOfReviews).isEqualTo(UPDATED_NUMBER_OF_REVIEWS)
        assertThat(testRestaurant.priceLevel).isEqualTo(UPDATED_PRICE_LEVEL)
        assertThat(testRestaurant.permanentlyClosed).isEqualTo(UPDATED_PERMANENTLY_CLOSED)

        // Validate the Restaurant in Elasticsearch
        verify(mockRestaurantSearchRepository, times(1)).save(testRestaurant)
    }

    @Test
    @Transactional
    fun updateNonExistingRestaurant() {
        val databaseSizeBeforeUpdate = restaurantRepository.findAll().size

        // Create the Restaurant
        val restaurantDTO = restaurantMapper.toDto(restaurant)

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRestaurantMockMvc.perform(
            put("/api/restaurants")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(restaurantDTO))
        ).andExpect(status().isBadRequest)

        // Validate the Restaurant in the database
        val restaurantList = restaurantRepository.findAll()
        assertThat(restaurantList).hasSize(databaseSizeBeforeUpdate)

        // Validate the Restaurant in Elasticsearch
        verify(mockRestaurantSearchRepository, times(0)).save(restaurant)
    }

    @Test
    @Transactional
    fun deleteRestaurant() {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant)

        val databaseSizeBeforeDelete = restaurantRepository.findAll().size

        val id = restaurant.id
        assertNotNull(id)

        // Delete the restaurant
        restRestaurantMockMvc.perform(
            delete("/api/restaurants/{id}", id)
                .accept(APPLICATION_JSON_UTF8)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val restaurantList = restaurantRepository.findAll()
        assertThat(restaurantList).hasSize(databaseSizeBeforeDelete - 1)

        // Validate the Restaurant in Elasticsearch
        verify(mockRestaurantSearchRepository, times(1)).deleteById(id)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun searchRestaurant() {
        // InitializesearchRestaurant() the database
        restaurantRepository.saveAndFlush(restaurant)
        `when`(mockRestaurantSearchRepository.search(queryStringQuery("id:" + restaurant.id), PageRequest.of(0, 20)))
            .thenReturn(PageImpl(listOf(restaurant), PageRequest.of(0, 1), 1))
        // Search the restaurant
        restRestaurantMockMvc.perform(get("/api/_search/restaurants?query=id:" + restaurant.id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(restaurant.id?.toInt())))
            .andExpect(jsonPath("$.[*].placeId").value(hasItem(DEFAULT_PLACE_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].vicinity").value(hasItem(DEFAULT_VICINITY)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE)))
            .andExpect(jsonPath("$.[*].googleRating").value(hasItem(DEFAULT_GOOGLE_RATING.toInt())))
            .andExpect(jsonPath("$.[*].btRating").value(hasItem(DEFAULT_BT_RATING.toInt())))
            .andExpect(jsonPath("$.[*].numberOfReviews").value(hasItem(DEFAULT_NUMBER_OF_REVIEWS)))
            .andExpect(jsonPath("$.[*].priceLevel").value(hasItem(DEFAULT_PRICE_LEVEL)))
            .andExpect(jsonPath("$.[*].permanentlyClosed").value(hasItem(DEFAULT_PERMANENTLY_CLOSED)))
    }

    companion object {

        private const val DEFAULT_PLACE_ID = "AAAAAAAAAA"
        private const val UPDATED_PLACE_ID = "BBBBBBBBBB"

        private const val DEFAULT_NAME = "AAAAAAAAAA"
        private const val UPDATED_NAME = "BBBBBBBBBB"

        private const val DEFAULT_ADDRESS = "AAAAAAAAAA"
        private const val UPDATED_ADDRESS = "BBBBBBBBBB"

        private const val DEFAULT_VICINITY = "AAAAAAAAAA"
        private const val UPDATED_VICINITY = "BBBBBBBBBB"

        private const val DEFAULT_URL = "AAAAAAAAAA"
        private const val UPDATED_URL = "BBBBBBBBBB"

        private const val DEFAULT_WEBSITE = "AAAAAAAAAA"
        private const val UPDATED_WEBSITE = "BBBBBBBBBB"

        private val DEFAULT_GOOGLE_RATING: BigDecimal = BigDecimal(1)
        private val UPDATED_GOOGLE_RATING: BigDecimal = BigDecimal(2)

        private val DEFAULT_BT_RATING: BigDecimal = BigDecimal(1)
        private val UPDATED_BT_RATING: BigDecimal = BigDecimal(2)

        private const val DEFAULT_NUMBER_OF_REVIEWS: Int = 1
        private const val UPDATED_NUMBER_OF_REVIEWS: Int = 2

        private const val DEFAULT_PRICE_LEVEL = "AAAAAAAAAA"
        private const val UPDATED_PRICE_LEVEL = "BBBBBBBBBB"

        private const val DEFAULT_PERMANENTLY_CLOSED: Boolean = false
        private const val UPDATED_PERMANENTLY_CLOSED: Boolean = true

        /**
         * Create an entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createEntity(em: EntityManager): Restaurant {
            val restaurant = Restaurant(
                placeId = DEFAULT_PLACE_ID,
                name = DEFAULT_NAME,
                address = DEFAULT_ADDRESS,
                vicinity = DEFAULT_VICINITY,
                url = DEFAULT_URL,
                website = DEFAULT_WEBSITE,
                googleRating = DEFAULT_GOOGLE_RATING,
                btRating = DEFAULT_BT_RATING,
                numberOfReviews = DEFAULT_NUMBER_OF_REVIEWS,
                priceLevel = DEFAULT_PRICE_LEVEL,
                permanentlyClosed = DEFAULT_PERMANENTLY_CLOSED
            )

            // Add required entity
            val user = UserResourceIT.createEntity(em)
            em.persist(user)
            em.flush()
            restaurant.user = user
            return restaurant
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): Restaurant {
            val restaurant = Restaurant(
                placeId = UPDATED_PLACE_ID,
                name = UPDATED_NAME,
                address = UPDATED_ADDRESS,
                vicinity = UPDATED_VICINITY,
                url = UPDATED_URL,
                website = UPDATED_WEBSITE,
                googleRating = UPDATED_GOOGLE_RATING,
                btRating = UPDATED_BT_RATING,
                numberOfReviews = UPDATED_NUMBER_OF_REVIEWS,
                priceLevel = UPDATED_PRICE_LEVEL,
                permanentlyClosed = UPDATED_PERMANENTLY_CLOSED
            )

            // Add required entity
            val user = UserResourceIT.createEntity(em)
            em.persist(user)
            em.flush()
            restaurant.user = user
            return restaurant
        }
    }
}
