package com.grosslicht.burgertuesday.security

import com.auth0.spring.security.api.JwtWebSecurityConfigurer
import com.grosslicht.burgertuesday.config.ApplicationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@EnableWebSecurity
@Configuration
class SecurityConfig(
    private val applicationProperties: ApplicationProperties
) : WebSecurityConfigurerAdapter() {

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        JwtWebSecurityConfigurer
            .forRS256(applicationProperties.auth0.audience, applicationProperties.auth0.issuer)
            .configure(http)
            .authorizeRequests()
            .anyRequest()
            .authenticated()
    }
}
