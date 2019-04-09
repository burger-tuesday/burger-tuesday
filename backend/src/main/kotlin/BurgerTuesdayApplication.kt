package com.grosslicht.burgertuesday

import com.grosslicht.burgertuesday.config.ApplicationProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@EnableConfigurationProperties(ApplicationProperties::class)
@SpringBootApplication
class BurgerTuesdayApplication

fun main(args: Array<String>) {
runApplication<BurgerTuesdayApplication>(*args)
}
