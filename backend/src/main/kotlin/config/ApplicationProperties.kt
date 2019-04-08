package com.grosslicht.burgertuesday.config

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * Properties specific to Burgertuesday.
 *
 *
 * Properties are configured in the application.yml file.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
class ApplicationProperties {
    val auth0: Auth0 = Auth0()

    class Auth0 {
        lateinit var audience: String
        lateinit var issuer: String
    }
}
