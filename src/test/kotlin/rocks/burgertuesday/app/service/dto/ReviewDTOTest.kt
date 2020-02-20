package rocks.burgertuesday.app.service.dto

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import rocks.burgertuesday.app.web.rest.equalsVerifier

class ReviewDTOTest {

    @Test
    fun dtoEqualsVerifier() {
        equalsVerifier(ReviewDTO::class)
        val reviewDTO1 = ReviewDTO()
        reviewDTO1.id = 1L
        val reviewDTO2 = ReviewDTO()
        assertThat(reviewDTO1).isNotEqualTo(reviewDTO2)
        reviewDTO2.id = reviewDTO1.id
        assertThat(reviewDTO1).isEqualTo(reviewDTO2)
        reviewDTO2.id = 2L
        assertThat(reviewDTO1).isNotEqualTo(reviewDTO2)
        reviewDTO1.id = null
        assertThat(reviewDTO1).isNotEqualTo(reviewDTO2)
    }
}
