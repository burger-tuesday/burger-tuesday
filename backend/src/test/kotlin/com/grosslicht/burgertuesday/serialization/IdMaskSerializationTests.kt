package com.grosslicht.burgertuesday.serialization

import at.favre.lib.bytes.Bytes
import at.favre.lib.idmask.Config
import at.favre.lib.idmask.IdMask
import at.favre.lib.idmask.IdMasks
import at.favre.lib.idmask.KeyManager
import com.grosslicht.burgertuesday.config.ApplicationProperties
import com.grosslicht.burgertuesday.domain.Restaurant
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.json.JacksonTester
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Scope
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.math.BigDecimal
import kotlin.random.Random

@ExtendWith(SpringExtension::class)
@JsonTest
class BurgerTuesdayApplicationTests {
    @Autowired
    private lateinit var tester: JacksonTester<Restaurant>

    private val long = Random.nextLong()

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
    @Throws(Exception::class)
    fun testSerialize() {
        val restaurant = Restaurant(
            id = long,
            address = "Something",
            vicinity = "Something",
            priceLevel = 0,
            website = "https://test.com",
            url = "https://test1.com",
            placeId = "aA",
            name = "Name",
            googleRating = BigDecimal.ONE,
            createdBy = "somebody"
        )
        // Assert against a `.json` file in the same package as the test
        val jsonContent = this.tester.write(restaurant)
        assertThat(jsonContent).hasJsonPathStringValue("@.id").isNotEqualTo(long.toString())
        val parsedRestaurant = this.tester.parseObject(jsonContent.json)
        assertThat(parsedRestaurant.id).isEqualTo(long)
    }
}
