package rocks.burgertuesday.app.security.oauth2

import rocks.burgertuesday.app.security.extractAuthorityFromClaims
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.stereotype.Component

@Component
class JwtAuthorityExtractor : JwtAuthenticationConverter() {
    override fun extractAuthorities(jwt: Jwt) = extractAuthorityFromClaims(jwt.claims)
}
