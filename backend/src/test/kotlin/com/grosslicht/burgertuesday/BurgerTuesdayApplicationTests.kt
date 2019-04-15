package com.grosslicht.burgertuesday

import at.favre.lib.bytes.Bytes
import at.favre.lib.idmask.Config
import at.favre.lib.idmask.IdMask
import at.favre.lib.idmask.IdMasks
import at.favre.lib.idmask.KeyManager
import com.grosslicht.burgertuesday.config.ApplicationProperties
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Scope
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
class BurgerTuesdayApplicationTests {
    @TestConfiguration
    class IdMaskTestConfig {
        @Bean
        @Scope
        fun idMask(applicationProperties: ApplicationProperties): IdMask<Long> = IdMasks.forLongIds(
            Config.builder(
                KeyManager.Factory.with(
                    Bytes.parseHex("cdec7b51973dd4dd09d13c28b47b62d4").array()
                )
            ).build()
        )
    }

    @Test
    fun contextLoads() {
    }
}
