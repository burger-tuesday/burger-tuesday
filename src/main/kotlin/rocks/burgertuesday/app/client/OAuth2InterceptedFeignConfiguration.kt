package rocks.burgertuesday.app.client

import org.springframework.context.annotation.Bean

import rocks.burgertuesday.app.security.oauth2.AuthorizationHeaderUtil

class OAuth2InterceptedFeignConfiguration {

    @Bean(name = ["oauth2RequestInterceptor"])
    fun getOAuth2RequestInterceptor(authorizationHeaderUtil: AuthorizationHeaderUtil) =
        TokenRelayRequestInterceptor(authorizationHeaderUtil)
}
