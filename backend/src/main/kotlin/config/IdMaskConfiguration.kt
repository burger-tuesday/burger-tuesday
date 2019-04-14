package com.grosslicht.burgertuesday.config

import at.favre.lib.bytes.Bytes
import at.favre.lib.idmask.Config
import at.favre.lib.idmask.IdMask
import at.favre.lib.idmask.IdMasks
import at.favre.lib.idmask.KeyManager
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope

@Configuration
@ConditionalOnProperty(prefix = "application.idMask", name = ["secret"])
class IdMaskConfiguration {
    @Bean
    @Scope
    fun idMask(applicationProperties: ApplicationProperties): IdMask<Long> = IdMasks.forLongIds(
        Config.builder(
            KeyManager.Factory.with(
                Bytes.parseHex(
                    applicationProperties.idMask.secret
                ).array()
            )
        ).build()
    )
}
