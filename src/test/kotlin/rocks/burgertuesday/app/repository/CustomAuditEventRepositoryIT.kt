package rocks.burgertuesday.app.repository

import rocks.burgertuesday.app.BurgertuesdayApp
import rocks.burgertuesday.app.config.ANONYMOUS_USER
import rocks.burgertuesday.app.config.TestSecurityConfiguration
import rocks.burgertuesday.app.config.audit.AuditEventConverter
import rocks.burgertuesday.app.domain.PersistentAuditEvent
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.actuate.audit.AuditEvent
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpSession
import org.springframework.security.web.authentication.WebAuthenticationDetails
import org.springframework.transaction.annotation.Transactional

import java.time.Instant
import java.time.temporal.ChronoUnit

import org.assertj.core.api.Assertions.assertThat

/**
 * Integration tests for [CustomAuditEventRepository].
 */
@SpringBootTest(classes = [BurgertuesdayApp::class, TestSecurityConfiguration::class])
@Transactional
class CustomAuditEventRepositoryIT {

    @Autowired
    private lateinit var persistenceAuditEventRepository: PersistenceAuditEventRepository

    @Autowired
    private lateinit var auditEventConverter: AuditEventConverter

    private lateinit var customAuditEventRepository: CustomAuditEventRepository

    private lateinit var testUserEvent: PersistentAuditEvent

    private lateinit var testOtherUserEvent: PersistentAuditEvent

    private lateinit var testOldUserEvent: PersistentAuditEvent

    @BeforeEach
    fun setup() {
        customAuditEventRepository = CustomAuditEventRepository(persistenceAuditEventRepository, auditEventConverter)
        persistenceAuditEventRepository.deleteAll()
        val oneHourAgo = Instant.now().minusSeconds(3600)

        testUserEvent = PersistentAuditEvent(
            principal = "test-user",
            auditEventType = "test-type",
            auditEventDate = oneHourAgo,
            data = mutableMapOf("test-key" to "test-value")
        )

        testOldUserEvent = PersistentAuditEvent(
            principal = "test-user",
            auditEventType = "test-type",
            auditEventDate = oneHourAgo.minusSeconds(10000)
        )

        testOtherUserEvent = PersistentAuditEvent(
            principal = "other-test-user",
            auditEventType = "test-type",
            auditEventDate = oneHourAgo
        )
    }

    @Test
    fun addAuditEvent() {
        val data = mutableMapOf<String, Any>("test-key" to "test-value")
        val event = AuditEvent("test-user", "test-type", data)
        customAuditEventRepository.add(event)
        val persistentAuditEvents = persistenceAuditEventRepository.findAll()
        assertThat(persistentAuditEvents).hasSize(1)
        val persistentAuditEvent = persistentAuditEvents[0]
        assertThat(persistentAuditEvent.principal).isEqualTo(event.principal)
        assertThat(persistentAuditEvent.auditEventType).isEqualTo(event.type)
        assertThat(persistentAuditEvent.data).containsKey("test-key")
        assertThat(persistentAuditEvent.data["test-key"]).isEqualTo("test-value")
        assertThat(persistentAuditEvent.auditEventDate!!.truncatedTo(ChronoUnit.MILLIS))
            .isEqualTo(event.timestamp!!.truncatedTo(ChronoUnit.MILLIS))
    }

    @Test
    fun addAuditEventTruncateLargeData() {
        val data = mutableMapOf<String, Any>()
        val largeData = StringBuilder()
        for (i in 0 until EVENT_DATA_COLUMN_MAX_LENGTH + 10) {
            largeData.append("a")
        }
        data["test-key"] = largeData
        val event = AuditEvent("test-user", "test-type", data)
        customAuditEventRepository.add(event)
        val persistentAuditEvents = persistenceAuditEventRepository.findAll()
        assertThat(persistentAuditEvents).hasSize(1)
        val persistentAuditEvent = persistentAuditEvents[0]
        assertThat(persistentAuditEvent.principal).isEqualTo(event.principal)
        assertThat(persistentAuditEvent.auditEventType).isEqualTo(event.type)
        assertThat(persistentAuditEvent.data).containsKey("test-key")
        val actualData = persistentAuditEvent.data["test-key"]
        assertThat(actualData!!.length).isEqualTo(EVENT_DATA_COLUMN_MAX_LENGTH)
        assertThat(actualData).isSubstringOf(largeData)
        assertThat(persistentAuditEvent.auditEventDate!!.truncatedTo(ChronoUnit.MILLIS))
            .isEqualTo(event.timestamp!!.truncatedTo(ChronoUnit.MILLIS))
    }

    @Test
    fun testAddEventWithWebAuthenticationDetails() {
        val session = MockHttpSession(null, "test-session-id")
        val request = MockHttpServletRequest()
        request.session = session
        request.remoteAddr = "1.2.3.4"
        val details = WebAuthenticationDetails(request)
        val data = mutableMapOf<String, Any>("test-key" to details)
        val event = AuditEvent("test-user", "test-type", data)
        customAuditEventRepository.add(event)
        val persistentAuditEvents = persistenceAuditEventRepository.findAll()
        assertThat(persistentAuditEvents).hasSize(1)
        val persistentAuditEvent = persistentAuditEvents[0]
        assertThat(persistentAuditEvent.data["remoteAddress"]).isEqualTo("1.2.3.4")
        assertThat(persistentAuditEvent.data["sessionId"]).isEqualTo("test-session-id")
    }

    @Test
    fun testAddEventWithNullData() {
        val data = mutableMapOf<String, Any?>("test-key" to null)
        val event = AuditEvent("test-user", "test-type", data)
        customAuditEventRepository.add(event)
        val persistentAuditEvents = persistenceAuditEventRepository.findAll()
        assertThat(persistentAuditEvents).hasSize(1)
        val persistentAuditEvent = persistentAuditEvents[0]
        assertThat(persistentAuditEvent.data["test-key"]).isNull()
    }

    @Test
    fun addAuditEventWithAnonymousUser() {
        val data = mutableMapOf<String, Any>("test-key" to "test-value")
        val event = AuditEvent(ANONYMOUS_USER, "test-type", data)
        customAuditEventRepository.add(event)
        val persistentAuditEvents = persistenceAuditEventRepository.findAll()
        assertThat(persistentAuditEvents).hasSize(0)
    }

    @Test
    fun addAuditEventWithAuthorizationFailureType() {
        val data = mutableMapOf<String, Any>("test-key" to "test-value")
        val event = AuditEvent("test-user", "AUTHORIZATION_FAILURE", data)
        customAuditEventRepository.add(event)
        val persistentAuditEvents = persistenceAuditEventRepository.findAll()
        assertThat(persistentAuditEvents).hasSize(0)
    }
}
