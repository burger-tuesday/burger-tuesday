package rocks.burgertuesday.app.web.websocket.dto

import java.time.Instant

/**
 * DTO for storing a user's activity.
 */
data class ActivityDTO(
    var sessionId: String? = null,
    var userLogin: String? = null,
    var ipAddress: String? = null,
    var page: String? = null,
    var time: Instant? = null
)
