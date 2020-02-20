package rocks.burgertuesday.app.web.rest

import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.core.oidc.OidcIdToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

import javax.servlet.http.HttpServletRequest

/**
 * REST controller for managing global OIDC logout.
 */
@RestController
class LogoutResource(registrations: ClientRegistrationRepository) {
    private val registration: ClientRegistration = registrations.findByRegistrationId("oidc")

    /**
     * `POST  /api/logout` : logout the current user.
     *
     * @param request the [HttpServletRequest].
     * @param idToken the ID token.
     * @return the [ResponseEntity] with status `200 (OK)` and a body with a global logout URL and ID token.
     */
    @PostMapping("/api/logout")
    fun logout(
        request: HttpServletRequest,
        @AuthenticationPrincipal(expression = "idToken") idToken: OidcIdToken?
    ): ResponseEntity<*> {
        val logoutUrl = registration.providerDetails.configurationMetadata["end_session_endpoint"].toString()

        val logoutDetails = mutableMapOf(
            "logoutUrl" to logoutUrl,
            "idToken" to idToken?.tokenValue
        )
        request.session.invalidate()
        return ResponseEntity.ok().body(logoutDetails)
    }
}
