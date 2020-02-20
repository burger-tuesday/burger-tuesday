package rocks.burgertuesday.app.web.rest

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.test.context.TestSecurityContextHolder
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.context.WebApplicationContext
import rocks.burgertuesday.app.BurgertuesdayApp
import rocks.burgertuesday.app.config.TestSecurityConfiguration
import rocks.burgertuesday.app.security.ADMIN
import rocks.burgertuesday.app.service.UserService
import rocks.burgertuesday.app.web.rest.errors.ExceptionTranslator

/**
 * Integration tests for the [AccountResource] REST controller.
 */
@SpringBootTest(classes = [BurgertuesdayApp::class, TestSecurityConfiguration::class])
class AccountResourceIT {

    @Autowired
    private lateinit var exceptionTranslator: ExceptionTranslator

    @Autowired
    private lateinit var userService: UserService

    private lateinit var restUserMockMvc: MockMvc

    @Autowired
    private lateinit var context: WebApplicationContext

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val accountUserMockResource = AccountResource(userService)

        this.restUserMockMvc = MockMvcBuilders.standaloneSetup(accountUserMockResource)
            .setControllerAdvice(exceptionTranslator)
            .build()
    }

    @Test
    @Throws(Exception::class)
    fun testNonAuthenticatedUser() {
        restUserMockMvc.perform(
            get("/api/authenticate")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().string(""))
    }

    @Test
    @Throws(Exception::class)
    fun testAuthenticatedUser() {
        restUserMockMvc.perform(get("/api/authenticate")
            .with { request ->
                request.remoteUser = "test"
                request
            }
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().string("test"))
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun testGetExistingAccount() {
        // create security-aware mockMvc
        restUserMockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply<DefaultMockMvcBuilder>(springSecurity())
            .build()

        val userDetails = mapOf<String, Any>(
            "sub" to "test",
            "email" to "john.doe@burgertuesday.rocks"
        )
        val authorities = listOf(SimpleGrantedAuthority(ADMIN))
        val user = DefaultOAuth2User(authorities, userDetails, "sub")
        val authentication = OAuth2AuthenticationToken(user, authorities, "oidc")
        TestSecurityContextHolder.getContext().authentication = authentication

        restUserMockMvc.perform(
            get("/api/account")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("\$.login").value("test"))
            .andExpect(jsonPath("\$.email").value("john.doe@burgertuesday.rocks"))
            .andExpect(jsonPath("\$.authorities").value(ADMIN))
    }

    @Test
    @Throws(Exception::class)
    fun testGetUnknownAccount() {
        restUserMockMvc.perform(
            get("/api/account")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isInternalServerError)
    }
}
