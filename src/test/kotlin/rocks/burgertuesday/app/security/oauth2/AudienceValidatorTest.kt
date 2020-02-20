package rocks.burgertuesday.app.security.oauth2

import org.junit.jupiter.api.Test
import org.springframework.security.oauth2.jwt.Jwt

import org.assertj.core.api.Assertions.assertThat
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

/**
 * Test class for the [AudienceValidator] utility class.
 */
class AudienceValidatorTest {

    private val validator = AudienceValidator()

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
        val badJwt: Jwt = mock(Jwt::class.java)
        `when`(badJwt.audience).thenReturn(claims.values.toMutableList())
        assertThat(validator.validate(badJwt).hasErrors()).isFalse()
    }
}
