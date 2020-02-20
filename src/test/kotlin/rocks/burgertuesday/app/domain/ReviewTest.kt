package rocks.burgertuesday.app.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import rocks.burgertuesday.app.web.rest.equalsVerifier

class ReviewTest {

    @Test
    fun equalsVerifier() {
        equalsVerifier(Review::class)
        val review1 = Review()
        review1.id = 1L
        val review2 = Review()
        review2.id = review1.id
        assertThat(review1).isEqualTo(review2)
        review2.id = 2L
        assertThat(review1).isNotEqualTo(review2)
        review1.id = null
        assertThat(review1).isNotEqualTo(review2)
    }
}
