package rocks.burgertuesday.app.security.oauth2

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.springframework.security.oauth2.jwt.Jwt

/**
 * Test class for the [AudienceValidator] utility class.
 */
class AudienceValidatorTest {

    private val validator = AudienceValidator(listOf("api://default"))

    @Test
    fun testInvalidAudience() {
        val claims = mutableMapOf("aud" to "bar")
        val badJwt: Jwt = mock(Jwt::class.java)
        `when`(badJwt.audience).thenReturn(claims.values.toMutableList())
        assertThat(validator.validate(badJwt).hasErrors()).isTrue()
    }

    @Test
    fun testValidAudience() {
        val claims = mutableMapOf("aud" to "api://default")
        val jwt: Jwt = mock(Jwt::class.java)
        `when`(jwt.audience).thenReturn(claims.values.toMutableList())
        assertThat(validator.validate(jwt).hasErrors()).isFalse()
    }
}
