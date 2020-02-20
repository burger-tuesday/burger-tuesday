package rocks.burgertuesday.app.web.rest

import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Resource to return information about OIDC properties
 */
@RestController
@RequestMapping("/api")
class AuthInfoResource(
    @Value("\${spring.security.oauth2.client.provider.oidc.issuer-uri:}")
    private var issuer: String?,
    @Value("\${spring.security.oauth2.client.registration.oidc.client-id:}")
    private var clientId: String?
) {

    @GetMapping("/auth-info")
    fun getAuthInfo(): AuthInfoVM = AuthInfoVM(issuer, clientId)

    data class AuthInfoVM(private var issuer: String? = null, private var clientId: String? = null)
}
