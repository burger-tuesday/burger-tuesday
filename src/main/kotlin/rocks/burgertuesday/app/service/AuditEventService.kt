package rocks.burgertuesday.app.service

import rocks.burgertuesday.app.config.audit.AuditEventConverter
import rocks.burgertuesday.app.repository.PersistenceAuditEventRepository
import org.springframework.boot.actuate.audit.AuditEvent
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import java.time.Instant
import java.util.Optional

/**
 * Service for managing audit events.
 *
 * This is the default implementation to support SpringBoot Actuator `AuditEventRepository`.
 */
@Service
@Transactional
class AuditEventService(
    private val persistenceAuditEventRepository: PersistenceAuditEventRepository,
    private val auditEventConverter: AuditEventConverter
) {

    fun findAll(pageable: Pageable): Page<AuditEvent> =
        persistenceAuditEventRepository.findAll(pageable)
            .map { auditEventConverter.convertToAuditEvent(it) }

    fun findByDates(fromDate: Instant, toDate: Instant, pageable: Pageable): Page<AuditEvent> =
        persistenceAuditEventRepository.findAllByAuditEventDateBetween(fromDate, toDate, pageable)
            .map { auditEventConverter.convertToAuditEvent(it) }

    fun find(id: Long): Optional<AuditEvent> =
        Optional.ofNullable(persistenceAuditEventRepository.findById(id))
            .filter { it.isPresent }
            .map { it.get() }
            .map { auditEventConverter.convertToAuditEvent(it) }
}
