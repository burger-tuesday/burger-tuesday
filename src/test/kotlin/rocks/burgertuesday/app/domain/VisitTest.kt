package rocks.burgertuesday.app.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import rocks.burgertuesday.app.web.rest.equalsVerifier

class VisitTest {

    @Test
    fun equalsVerifier() {
        equalsVerifier(Visit::class)
        val visit1 = Visit()
        visit1.id = 1L
        val visit2 = Visit()
        visit2.id = visit1.id
        assertThat(visit1).isEqualTo(visit2)
        visit2.id = 2L
        assertThat(visit1).isNotEqualTo(visit2)
        visit1.id = null
        assertThat(visit1).isNotEqualTo(visit2)
    }
}
