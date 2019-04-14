package com.grosslicht.burgertuesday.config

import com.google.maps.GeoApiContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope

@Configuration
class GoogleApiConfiguration {
    @Bean
    @Scope
    fun geoApiContext(applicationProperties: ApplicationProperties): GeoApiContext =
        GeoApiContext.Builder().apiKey(applicationProperties.google.apiKey).build()
}
