@file:JvmName("SecurityUtils")

package rocks.burgertuesday.app.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken

import java.util.Optional

/**
 * Get the login of the current user.
 *
 * @return the login of the current user.
 */
fun getCurrentUserLogin(): Optional<String> =
    Optional.ofNullable(SecurityContextHolder.getContext().authentication)
        .map { authentication ->
            when (val principal = authentication.principal) {
                is UserDetails -> principal.username
                is JwtAuthenticationToken -> (authentication as JwtAuthenticationToken).token.claims as String
                is DefaultOidcUser -> {
                    if (principal.attributes.containsKey("preferred_username")) {
                        principal.attributes["preferred_username"].toString()
                    } else {
                        null
                    }
                }
                is String -> principal
                else -> null
            }
        }

/**
 * Check if a user is authenticated.
 *
 * @return true if the user is authenticated, false otherwise.
 */
fun isAuthenticated(): Boolean =
    Optional.ofNullable(SecurityContextHolder.getContext().authentication)
        .map {
            val authorities = when (it) {
                    is JwtAuthenticationToken ->
                        extractAuthorityFromClaims(it.token.claims)
                    else ->
                        it.authorities
                }
            authorities.none { authority -> authority.authority == ANONYMOUS }
        }
        .orElse(false)

/**
 * If the current user has a specific authority (security role).
 *
 * The name of this method comes from the `isUserInRole()` method in the Servlet API
 *
 * @param authority the authority to check.
 * @return true if the current user has the authority, false otherwise.
 */
fun isCurrentUserInRole(authority: String): Boolean =
    Optional.ofNullable(SecurityContextHolder.getContext().authentication)
        .map {
            val authorities = when (it) {
                    is JwtAuthenticationToken ->
                        extractAuthorityFromClaims(it.token.claims)
                    else ->
                        it.authorities
                }
            authorities.any { auth -> auth.authority.equals(authority) }
        }
        .orElse(false)

    fun extractAuthorityFromClaims(claims: Map<String, Any>): List<GrantedAuthority> {
        return mapRolesToGrantedAuthorities(getRolesFromClaims(claims))
    }

    @Suppress("UNCHECKED_CAST")
    fun getRolesFromClaims(claims: Map<String, Any>): Collection<String> {
        return claims.getOrDefault("groups", claims.getOrDefault("roles", listOf<String>())) as Collection<String>
    }

    fun mapRolesToGrantedAuthorities(roles: Collection<String>): List<GrantedAuthority> {
        return roles
            .filter { it.startsWith("ROLE_") }
            .map { SimpleGrantedAuthority(it) }
    }
