package rocks.burgertuesday.app.service.mapper

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ReviewMapperTest {

    private lateinit var reviewMapper: ReviewMapper

    @BeforeEach
    fun setUp() {
        reviewMapper = ReviewMapperImpl()
    }

    @Test
    fun testEntityFromId() {
        val id = 2L
        assertThat(reviewMapper.fromId(id)?.id).isEqualTo(id)
        assertThat(reviewMapper.fromId(null)).isNull()
    }
}
