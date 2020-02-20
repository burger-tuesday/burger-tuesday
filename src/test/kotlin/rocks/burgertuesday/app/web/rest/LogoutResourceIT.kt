package rocks.burgertuesday.app.web.rest

import rocks.burgertuesday.app.BurgertuesdayApp
import rocks.burgertuesday.app.config.TestSecurityConfiguration
import rocks.burgertuesday.app.security.USER
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.core.oidc.OidcIdToken
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

import java.time.Instant

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

private const val ID_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9" +
    ".eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsIm" +
    "p0aSI6ImQzNWRmMTRkLTA5ZjYtNDhmZi04YTkzLTdjNmYwMzM5MzE1OSIsImlhdCI6MTU0M" +
    "Tk3MTU4MywiZXhwIjoxNTQxOTc1MTgzfQ.QaQOarmV8xEUYV7yvWzX3cUE_4W1luMcWCwpr" +
    "oqqUrg"

/**
 * Integration tests for the [LogoutResource] REST controller.
 */
@SpringBootTest(classes = [BurgertuesdayApp::class, TestSecurityConfiguration::class])
class LogoutResourceIT {

    @Autowired
    private lateinit var registrations: ClientRegistrationRepository

    @Autowired
    private lateinit var context: WebApplicationContext

    private lateinit var restLogoutMockMvc: MockMvc

    @BeforeEach
    fun before() {
        val claims = mapOf(
            "groups" to "ROLE_USER",
            "sub" to 123
        )
        val idToken = OidcIdToken(ID_TOKEN, Instant.now(), Instant.now().plusSeconds(60), claims)
        SecurityContextHolder.getContext().authentication = authenticationToken(idToken)
        val authInjector = SecurityContextHolderAwareRequestFilter()
        authInjector.afterPropertiesSet()

        restLogoutMockMvc = MockMvcBuilders.webAppContextSetup(this.context).build()
    }

    @Test
    @Throws(Exception::class)
    fun getLogoutInformation() {
        val logoutUrl = this.registrations.findByRegistrationId("oidc").providerDetails
            .configurationMetadata["end_session_endpoint"].toString()
        restLogoutMockMvc.perform(post("/api/logout"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("\$.logoutUrl").value(logoutUrl))
            .andExpect(jsonPath("\$.idToken").value(ID_TOKEN))
    }
}

private fun authenticationToken(idToken: OidcIdToken): OAuth2AuthenticationToken {
    val authorities = listOf(SimpleGrantedAuthority(USER))
    val user = DefaultOidcUser(authorities, idToken)
    return OAuth2AuthenticationToken(user, authorities, "oidc")
}
