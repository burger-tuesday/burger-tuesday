package rocks.burgertuesday.app.service

import rocks.burgertuesday.app.service.dto.VisitDTO

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

import java.util.Optional

/**
 * Service Interface for managing [rocks.burgertuesday.app.domain.Visit].
 */
interface VisitService {

    /**
     * Save a visit.
     *
     * @param visitDTO the entity to save.
     * @return the persisted entity.
     */
    fun save(visitDTO: VisitDTO): VisitDTO

    /**
     * Get all the visits.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    fun findAll(pageable: Pageable): Page<VisitDTO>

    /**
     * Get the "id" visit.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: Long): Optional<VisitDTO>

    /**
     * Delete the "id" visit.
     *
     * @param id the id of the entity.
     */
    fun delete(id: Long)

    /**
     * Search for the visit corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    fun search(query: String, pageable: Pageable): Page<VisitDTO>
}
