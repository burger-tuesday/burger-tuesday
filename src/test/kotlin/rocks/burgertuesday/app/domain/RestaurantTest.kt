package rocks.burgertuesday.app.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import rocks.burgertuesday.app.web.rest.equalsVerifier

class RestaurantTest {

    @Test
    fun equalsVerifier() {
        equalsVerifier(Restaurant::class)
        val restaurant1 = Restaurant()
        restaurant1.id = 1L
        val restaurant2 = Restaurant()
        restaurant2.id = restaurant1.id
        assertThat(restaurant1).isEqualTo(restaurant2)
        restaurant2.id = 2L
        assertThat(restaurant1).isNotEqualTo(restaurant2)
        restaurant1.id = null
        assertThat(restaurant1).isNotEqualTo(restaurant2)
    }
}
