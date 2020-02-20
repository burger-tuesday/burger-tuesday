package rocks.burgertuesday.app.security.oauth2

import org.slf4j.LoggerFactory
import org.springframework.security.oauth2.core.OAuth2Error
import org.springframework.security.oauth2.core.OAuth2TokenValidator
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.util.Assert

class AudienceValidator(val allowedAudience: List<String>) : OAuth2TokenValidator<Jwt> {
    private val log = LoggerFactory.getLogger(javaClass)
    private val error = OAuth2Error("invalid_token", "The required audience is missing", null)

    init {
        Assert.notEmpty(allowedAudience, "Allowed audience should not be null or empty.")
    }

    override fun validate(jwt: Jwt): OAuth2TokenValidatorResult {
        val audience = jwt.audience
        return if (audience.any { allowedAudience.contains(it) }) {
            OAuth2TokenValidatorResult.success()
        } else {
            log.warn("Invalid audience: {}", audience)
            OAuth2TokenValidatorResult.failure(error)
        }
    }
}
