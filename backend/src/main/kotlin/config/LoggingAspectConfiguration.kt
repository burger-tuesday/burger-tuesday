package com.grosslicht.burgertuesday.config

import com.grosslicht.burgertuesday.aop.LoggingAspect
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.context.annotation.Profile
import org.springframework.core.env.Environment

@Configuration
@EnableAspectJAutoProxy
class LoggingAspectConfiguration {

    @Bean
    @Profile(BurgerTuesdayConstants.DEVELOPMENT_PROFILE)
    fun loggingAspect(env: Environment): LoggingAspect {
        return LoggingAspect(env)
    }
}
