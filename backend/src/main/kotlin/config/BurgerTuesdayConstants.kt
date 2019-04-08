package com.grosslicht.burgertuesday.config

import org.springframework.core.env.Profiles

object BurgerTuesdayConstants {
    const val DEVELOPMENT_PROFILE = "dev"
    val DEVELOPMENT_SPRING_PROFILE = Profiles.of(DEVELOPMENT_PROFILE)
}
