package rocks.burgertuesday.app.service.impl

import rocks.burgertuesday.app.service.VisitService
import rocks.burgertuesday.app.domain.Visit
import rocks.burgertuesday.app.repository.VisitRepository
import rocks.burgertuesday.app.repository.search.VisitSearchRepository
import rocks.burgertuesday.app.service.dto.VisitDTO
import rocks.burgertuesday.app.service.mapper.VisitMapper
import org.slf4j.LoggerFactory

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import java.util.Optional

import org.elasticsearch.index.query.QueryBuilders.queryStringQuery

/**
 * Service Implementation for managing [Visit].
 */
@Service
@Transactional
class VisitServiceImpl(
    private val visitRepository: VisitRepository,
    private val visitMapper: VisitMapper,
    private val visitSearchRepository: VisitSearchRepository
) : VisitService {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Save a visit.
     *
     * @param visitDTO the entity to save.
     * @return the persisted entity.
     */
    override fun save(visitDTO: VisitDTO): VisitDTO {
        log.debug("Request to save Visit : {}", visitDTO)

        var visit = visitMapper.toEntity(visitDTO)
        visit = visitRepository.save(visit)
        val result = visitMapper.toDto(visit)
        visitSearchRepository.save(visit)
        return result
    }

    /**
     * Get all the visits.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable): Page<VisitDTO> {
        log.debug("Request to get all Visits")
        return visitRepository.findAll(pageable)
            .map(visitMapper::toDto)
    }

    /**
     * Get one visit by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    override fun findOne(id: Long): Optional<VisitDTO> {
        log.debug("Request to get Visit : {}", id)
        return visitRepository.findById(id)
            .map(visitMapper::toDto)
    }

    /**
     * Delete the visit by id.
     *
     * @param id the id of the entity.
     */
    override fun delete(id: Long) {
        log.debug("Request to delete Visit : {}", id)

        visitRepository.deleteById(id)
        visitSearchRepository.deleteById(id)
    }

    /**
     * Search for the visit corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    override fun search(query: String, pageable: Pageable): Page<VisitDTO> {
        log.debug("Request to search for a page of Visits for query {}", query)
        return visitSearchRepository.search(queryStringQuery(query), pageable).map(visitMapper::toDto)
    }
}
