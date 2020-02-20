package rocks.burgertuesday.app.service.mapper

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class VisitMapperTest {

    private lateinit var visitMapper: VisitMapper

    @BeforeEach
    fun setUp() {
        visitMapper = VisitMapperImpl()
    }

    @Test
    fun testEntityFromId() {
        val id = 2L
        assertThat(visitMapper.fromId(id)?.id).isEqualTo(id)
        assertThat(visitMapper.fromId(null)).isNull()
    }
}
