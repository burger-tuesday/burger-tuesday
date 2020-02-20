package rocks.burgertuesday.app.web.rest

import java.time.LocalDate
import java.time.ZoneId
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
import rocks.burgertuesday.app.domain.Visit
import rocks.burgertuesday.app.repository.VisitRepository
import rocks.burgertuesday.app.repository.search.VisitSearchRepository
import rocks.burgertuesday.app.service.VisitService
import rocks.burgertuesday.app.service.mapper.VisitMapper
import rocks.burgertuesday.app.web.rest.errors.ExceptionTranslator

/**
 * Integration tests for the [VisitResource] REST controller.
 *
 * @see VisitResource
 */
@SpringBootTest(classes = [BurgertuesdayApp::class, TestSecurityConfiguration::class])
class VisitResourceIT {

    @Autowired
    private lateinit var visitRepository: VisitRepository

    @Autowired
    private lateinit var visitMapper: VisitMapper

    @Autowired
    private lateinit var visitService: VisitService

    /**
     * This repository is mocked in the rocks.burgertuesday.app.repository.search test package.
     *
     * @see rocks.burgertuesday.app.repository.search.VisitSearchRepositoryMockConfiguration
     */
    @Autowired
    private lateinit var mockVisitSearchRepository: VisitSearchRepository

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

    private lateinit var restVisitMockMvc: MockMvc

    private lateinit var visit: Visit

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val visitResource = VisitResource(visitService)
        this.restVisitMockMvc = MockMvcBuilders.standaloneSetup(visitResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build()
    }

    @BeforeEach
    fun initTest() {
        visit = createEntity(em)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createVisit() {
        val databaseSizeBeforeCreate = visitRepository.findAll().size

        // Create the Visit
        val visitDTO = visitMapper.toDto(visit)
        restVisitMockMvc.perform(
            post("/api/visits")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(visitDTO))
        ).andExpect(status().isCreated)

        // Validate the Visit in the database
        val visitList = visitRepository.findAll()
        assertThat(visitList).hasSize(databaseSizeBeforeCreate + 1)
        val testVisit = visitList[visitList.size - 1]
        assertThat(testVisit.date).isEqualTo(DEFAULT_DATE)
        assertThat(testVisit.sponsored).isEqualTo(DEFAULT_SPONSORED)

        // Validate the Visit in Elasticsearch
    }

    @Test
    @Transactional
    fun createVisitWithExistingId() {
        val databaseSizeBeforeCreate = visitRepository.findAll().size

        // Create the Visit with an existing ID
        visit.id = 1L
        val visitDTO = visitMapper.toDto(visit)

        // An entity with an existing ID cannot be created, so this API call must fail
        restVisitMockMvc.perform(
            post("/api/visits")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(visitDTO))
        ).andExpect(status().isBadRequest)

        // Validate the Visit in the database
        val visitList = visitRepository.findAll()
        assertThat(visitList).hasSize(databaseSizeBeforeCreate)

        // Validate the Visit in Elasticsearch
        verify(mockVisitSearchRepository, times(0)).save(visit)
    }

    @Test
    @Transactional
    fun getAllVisits() {
        // Initialize the database
        visitRepository.saveAndFlush(visit)

        // Get all the visitList
        restVisitMockMvc.perform(get("/api/visits?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(visit.id?.toInt())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].sponsored").value(hasItem(DEFAULT_SPONSORED)))
    }

    @Test
    @Transactional
    fun getVisit() {
        // Initialize the database
        visitRepository.saveAndFlush(visit)

        val id = visit.id
        assertNotNull(id)

        // Get the visit
        restVisitMockMvc.perform(get("/api/visits/{id}", id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(id.toInt()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.sponsored").value(DEFAULT_SPONSORED))
    }

    @Test
    @Transactional
    fun getNonExistingVisit() {
        // Get the visit
        restVisitMockMvc.perform(get("/api/visits/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }
    @Test
    @Transactional
    fun updateVisit() {
        // Initialize the database
        visitRepository.saveAndFlush(visit)

        val databaseSizeBeforeUpdate = visitRepository.findAll().size

        // Update the visit
        val id = visit.id
        assertNotNull(id)
        val updatedVisit = visitRepository.findById(id).get()
        // Disconnect from session so that the updates on updatedVisit are not directly saved in db
        em.detach(updatedVisit)
        updatedVisit.date = UPDATED_DATE
        updatedVisit.sponsored = UPDATED_SPONSORED
        val visitDTO = visitMapper.toDto(updatedVisit)

        restVisitMockMvc.perform(
            put("/api/visits")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(visitDTO))
        ).andExpect(status().isOk)

        // Validate the Visit in the database
        val visitList = visitRepository.findAll()
        assertThat(visitList).hasSize(databaseSizeBeforeUpdate)
        val testVisit = visitList[visitList.size - 1]
        assertThat(testVisit.date).isEqualTo(UPDATED_DATE)
        assertThat(testVisit.sponsored).isEqualTo(UPDATED_SPONSORED)

        // Validate the Visit in Elasticsearch
        verify(mockVisitSearchRepository, times(1)).save(testVisit)
    }

    @Test
    @Transactional
    fun updateNonExistingVisit() {
        val databaseSizeBeforeUpdate = visitRepository.findAll().size

        // Create the Visit
        val visitDTO = visitMapper.toDto(visit)

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVisitMockMvc.perform(
            put("/api/visits")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(visitDTO))
        ).andExpect(status().isBadRequest)

        // Validate the Visit in the database
        val visitList = visitRepository.findAll()
        assertThat(visitList).hasSize(databaseSizeBeforeUpdate)

        // Validate the Visit in Elasticsearch
        verify(mockVisitSearchRepository, times(0)).save(visit)
    }

    @Test
    @Transactional
    fun deleteVisit() {
        // Initialize the database
        visitRepository.saveAndFlush(visit)

        val databaseSizeBeforeDelete = visitRepository.findAll().size

        val id = visit.id
        assertNotNull(id)

        // Delete the visit
        restVisitMockMvc.perform(
            delete("/api/visits/{id}", id)
                .accept(APPLICATION_JSON_UTF8)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val visitList = visitRepository.findAll()
        assertThat(visitList).hasSize(databaseSizeBeforeDelete - 1)

        // Validate the Visit in Elasticsearch
        verify(mockVisitSearchRepository, times(1)).deleteById(id)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun searchVisit() {
        // InitializesearchVisit() the database
        visitRepository.saveAndFlush(visit)
        `when`(mockVisitSearchRepository.search(queryStringQuery("id:" + visit.id), PageRequest.of(0, 20)))
            .thenReturn(PageImpl(listOf(visit), PageRequest.of(0, 1), 1))
        // Search the visit
        restVisitMockMvc.perform(get("/api/_search/visits?query=id:" + visit.id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(visit.id?.toInt())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].sponsored").value(hasItem(DEFAULT_SPONSORED)))
    }

    companion object {

        private val DEFAULT_DATE: LocalDate = LocalDate.ofEpochDay(0L)
        private val UPDATED_DATE: LocalDate = LocalDate.now(ZoneId.systemDefault())

        private const val DEFAULT_SPONSORED: Boolean = false
        private const val UPDATED_SPONSORED: Boolean = true

        /**
         * Create an entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createEntity(em: EntityManager): Visit {
            val visit = Visit(
                date = DEFAULT_DATE,
                sponsored = DEFAULT_SPONSORED
            )

            // Add required entity
            val restaurant: Restaurant
            if (em.findAll(Restaurant::class).isEmpty()) {
                restaurant = RestaurantResourceIT.createEntity(em)
                em.persist(restaurant)
                em.flush()
            } else {
                restaurant = em.findAll(Restaurant::class).get(0)
            }
            visit.restaurant = restaurant
            // Add required entity
            val user = UserResourceIT.createEntity(em)
            em.persist(user)
            em.flush()
            visit.user = user
            return visit
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): Visit {
            val visit = Visit(
                date = UPDATED_DATE,
                sponsored = UPDATED_SPONSORED
            )

            // Add required entity
            val restaurant: Restaurant
            if (em.findAll(Restaurant::class).isEmpty()) {
                restaurant = RestaurantResourceIT.createUpdatedEntity(em)
                em.persist(restaurant)
                em.flush()
            } else {
                restaurant = em.findAll(Restaurant::class).get(0)
            }
            visit.restaurant = restaurant
            // Add required entity
            val user = UserResourceIT.createEntity(em)
            em.persist(user)
            em.flush()
            visit.user = user
            return visit
        }
    }
}
