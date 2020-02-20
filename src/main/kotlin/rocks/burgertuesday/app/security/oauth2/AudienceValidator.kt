package rocks.burgertuesday.app.security.oauth2

import org.slf4j.LoggerFactory
import org.springframework.security.oauth2.core.OAuth2Error
import org.springframework.security.oauth2.core.OAuth2TokenValidator
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult
import org.springframework.security.oauth2.jwt.Jwt

class AudienceValidator : OAuth2TokenValidator<Jwt> {
    private val log = LoggerFactory.getLogger(javaClass)
    private val error = OAuth2Error("invalid_token", "The required audience is missing", null)

    override fun validate(jwt: Jwt): OAuth2TokenValidatorResult {
        val audience = jwt.audience
        // Keycloak and Okta's default audiences, respectively
        return if (audience.contains("account") || audience.contains("api://default")) {
            OAuth2TokenValidatorResult.success()
        } else {
            log.warn("Invalid audience: {}", audience)
            OAuth2TokenValidatorResult.failure(error)
        }
    }
}
