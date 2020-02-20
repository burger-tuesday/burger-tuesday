package rocks.burgertuesday.app.service.dto

import java.time.LocalDate
import java.io.Serializable

/**
 * A DTO for the [rocks.burgertuesday.app.domain.Visit] entity.
 */
data class VisitDTO(

    var id: Long? = null,

    var date: LocalDate? = null,

    var sponsored: Boolean? = null,

    var restaurantId: Long? = null,

    var userId: String? = null

) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is VisitDTO) return false
        if (other.id == null || id == null) return false

        return id == other.id
    }

    override fun hashCode() = id.hashCode()
}
