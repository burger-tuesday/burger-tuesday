package rocks.burgertuesday.app.web.rest

import rocks.burgertuesday.app.service.VisitService
import rocks.burgertuesday.app.web.rest.errors.BadRequestAlertException
import rocks.burgertuesday.app.service.dto.VisitDTO

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

private const val ENTITY_NAME = "visit"

/**
 * REST controller for managing [rocks.burgertuesday.app.domain.Visit].
 */
@RestController
@RequestMapping("/api")
class VisitResource(
    private val visitService: VisitService
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /visits` : Create a new visit.
     *
     * @param visitDTO the visitDTO to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new visitDTO, or with status `400 (Bad Request)` if the visit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/visits")
    @PreAuthorize("hasRole(\"$ADMIN\")")
    fun createVisit(@Valid @RequestBody visitDTO: VisitDTO): ResponseEntity<VisitDTO> {
        log.debug("REST request to save Visit : {}", visitDTO)
        if (visitDTO.id != null) {
            throw BadRequestAlertException(
                "A new visit cannot already have an ID",
                ENTITY_NAME, "idexists"
            )
        }
        val result = visitService.save(visitDTO)
        return ResponseEntity.created(URI("/api/visits/" + result.id))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * `PUT  /visits` : Updates an existing visit.
     *
     * @param visitDTO the visitDTO to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated visitDTO,
     * or with status `400 (Bad Request)` if the visitDTO is not valid,
     * or with status `500 (Internal Server Error)` if the visitDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/visits")
    @PreAuthorize("hasRole(\"$ADMIN\")")
    fun updateVisit(@Valid @RequestBody visitDTO: VisitDTO): ResponseEntity<VisitDTO> {
        log.debug("REST request to update Visit : {}", visitDTO)
        if (visitDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        val result = visitService.save(visitDTO)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName, true, ENTITY_NAME, visitDTO.id.toString()
                )
            )
            .body(result)
    }

    /**
     * `GET  /visits` : get all the visits.
     *

     * @param pageable the pagination information.

     * @return the [ResponseEntity] with status `200 (OK)` and the list of visits in body.
     */
    @GetMapping("/visits")
    fun getAllVisits(
        pageable: Pageable
    ): ResponseEntity<MutableList<VisitDTO>> {
        log.debug("REST request to get a page of Visits")
        val page = visitService.findAll(pageable)
        val headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }

    /**
     * `GET  /visits/:id` : get the "id" visit.
     *
     * @param id the id of the visitDTO to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the visitDTO, or with status `404 (Not Found)`.
     */
    @GetMapping("/visits/{id}")
    fun getVisit(@PathVariable id: Long): ResponseEntity<VisitDTO> {
        log.debug("REST request to get Visit : {}", id)
        val visitDTO = visitService.findOne(id)
        return ResponseUtil.wrapOrNotFound(visitDTO)
    }

    /**
     *  `DELETE  /visits/:id` : delete the "id" visit.
     *
     * @param id the id of the visitDTO to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/visits/{id}")
    @PreAuthorize("hasRole(\"$ADMIN\")")
    fun deleteVisit(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete Visit : {}", id)
        visitService.delete(id)
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }

    /**
     * `SEARCH  /_search/visits?query=:query` : search for the visit corresponding
     * to the query.
     *
     * @param query the query of the visit search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/visits")
    fun searchVisits(@RequestParam query: String, pageable: Pageable): ResponseEntity<MutableList<VisitDTO>> {
        log.debug("REST request to search for a page of Visits for query {}", query)
        val page = visitService.search(query, pageable)
        val headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }
}
