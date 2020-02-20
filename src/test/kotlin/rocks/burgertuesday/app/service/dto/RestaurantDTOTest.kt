package rocks.burgertuesday.app.service.dto

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import rocks.burgertuesday.app.web.rest.equalsVerifier

class RestaurantDTOTest {

    @Test
    fun dtoEqualsVerifier() {
        equalsVerifier(RestaurantDTO::class)
        val restaurantDTO1 = RestaurantDTO()
        restaurantDTO1.id = 1L
        val restaurantDTO2 = RestaurantDTO()
        assertThat(restaurantDTO1).isNotEqualTo(restaurantDTO2)
        restaurantDTO2.id = restaurantDTO1.id
        assertThat(restaurantDTO1).isEqualTo(restaurantDTO2)
        restaurantDTO2.id = 2L
        assertThat(restaurantDTO1).isNotEqualTo(restaurantDTO2)
        restaurantDTO1.id = null
        assertThat(restaurantDTO1).isNotEqualTo(restaurantDTO2)
    }
}
