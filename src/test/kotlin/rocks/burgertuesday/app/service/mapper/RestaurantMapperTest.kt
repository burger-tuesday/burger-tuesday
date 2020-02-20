package rocks.burgertuesday.app.service.mapper

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RestaurantMapperTest {

    private lateinit var restaurantMapper: RestaurantMapper

    @BeforeEach
    fun setUp() {
        restaurantMapper = RestaurantMapperImpl()
    }

    @Test
    fun testEntityFromId() {
        val id = 2L
        assertThat(restaurantMapper.fromId(id)?.id).isEqualTo(id)
        assertThat(restaurantMapper.fromId(null)).isNull()
    }
}
