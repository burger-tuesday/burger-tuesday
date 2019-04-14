package com.grosslicht.burgertuesday.config

import com.grosslicht.burgertuesday.api.auth0.Auth0Api
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

@Configuration
class Auth0ApiConfiguration {
    @Bean
    @Scope
    fun auth0Api(): Auth0Api {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://burgertuesday.eu.auth0.com/")
            .addConverterFactory(JacksonConverterFactory.create())
            .build()

        return retrofit.create(Auth0Api::class.java)
    }
}
