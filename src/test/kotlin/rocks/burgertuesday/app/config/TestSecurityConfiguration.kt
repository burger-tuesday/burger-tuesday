package rocks.burgertuesday.app.config

import org.mockito.Mockito.mock
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository
import org.springframework.security.oauth2.client.web.AuthenticatedPrincipalOAuth2AuthorizedClientRepository
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.jwt.JwtDecoder

/**
 * This class allows you to run unit and integration tests without an IdP.
 */
@TestConfiguration
class TestSecurityConfiguration {
    private val clientRegistration: ClientRegistration = clientRegistration().build()

    @Bean
    fun clientRegistrationRepository() = InMemoryClientRegistrationRepository(clientRegistration)

    private fun clientRegistration(): ClientRegistration.Builder {
        val metadata = mutableMapOf<String, Any>()
        metadata["end_session_endpoint"] = "https://jhipster.org/logout"

        return ClientRegistration.withRegistrationId("oidc")
            .redirectUriTemplate("{baseUrl}/{action}/oauth2/code/{registrationId}")
            .clientAuthenticationMethod(ClientAuthenticationMethod.BASIC)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .scope("read:user")
            .authorizationUri("https://jhipster.org/login/oauth/authorize")
            .tokenUri("https://jhipster.org/login/oauth/access_token")
            .jwkSetUri("https://jhipster.org/oauth/jwk")
            .userInfoUri("https://api.jhipster.org/user")
            .providerConfigurationMetadata(metadata)
            .userNameAttributeName("id")
            .clientName("Client Name")
            .clientId("client-id")
            .clientSecret("client-secret")
    }

    @Bean
    fun jwtDecoder(): JwtDecoder = mock(JwtDecoder::class.java)

    @Bean
    fun authorizedClientService(clientRegistrationRepository: ClientRegistrationRepository) =
        InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository)

    @Bean
    fun authorizedClientRepository(authorizedClientService: OAuth2AuthorizedClientService) =
        AuthenticatedPrincipalOAuth2AuthorizedClientRepository(authorizedClientService)
}
