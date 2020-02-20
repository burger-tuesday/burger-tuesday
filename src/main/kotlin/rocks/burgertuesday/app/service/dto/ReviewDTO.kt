package rocks.burgertuesday.app.service.dto

import java.io.Serializable

/**
 * A DTO for the [rocks.burgertuesday.app.domain.Review] entity.
 */
data class ReviewDTO(

    var id: Long? = null,

    var review: String? = null,

    var taste: Int? = null,

    var likeness: Int? = null,

    var menuDiversity: Int? = null,

    var service: Int? = null,

    var priceLevel: Int? = null,

    var recommended: Boolean? = null,

    var visitId: Long? = null,

    var userId: String? = null

) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ReviewDTO) return false
        if (other.id == null || id == null) return false

        return id == other.id
    }

    override fun hashCode() = id.hashCode()
}
