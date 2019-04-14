package com.grosslicht.burgertuesday.config

import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JacksonConfiguration {

    /**
     * Support for Java date and time API.
     * @return the corresponding Jackson module.
     */
    @Bean
    fun javaTimeModule(): JavaTimeModule = JavaTimeModule()

    @Bean
    fun jdk8TimeModule(): Jdk8Module = Jdk8Module()
}
