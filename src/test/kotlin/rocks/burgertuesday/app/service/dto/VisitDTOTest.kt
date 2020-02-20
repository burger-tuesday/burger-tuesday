package rocks.burgertuesday.app.service.dto

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import rocks.burgertuesday.app.web.rest.equalsVerifier

class VisitDTOTest {

    @Test
    fun dtoEqualsVerifier() {
        equalsVerifier(VisitDTO::class)
        val visitDTO1 = VisitDTO()
        visitDTO1.id = 1L
        val visitDTO2 = VisitDTO()
        assertThat(visitDTO1).isNotEqualTo(visitDTO2)
        visitDTO2.id = visitDTO1.id
        assertThat(visitDTO1).isEqualTo(visitDTO2)
        visitDTO2.id = 2L
        assertThat(visitDTO1).isNotEqualTo(visitDTO2)
        visitDTO1.id = null
        assertThat(visitDTO1).isNotEqualTo(visitDTO2)
    }
}
