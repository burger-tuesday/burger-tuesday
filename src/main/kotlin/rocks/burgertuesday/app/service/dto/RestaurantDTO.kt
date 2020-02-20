package rocks.burgertuesday.app.service.dto

import java.io.Serializable
import java.math.BigDecimal

/**
 * A DTO for the [rocks.burgertuesday.app.domain.Restaurant] entity.
 */
data class RestaurantDTO(

    var id: Long? = null,

    var placeId: String? = null,

    var name: String? = null,

    var address: String? = null,

    var vicinity: String? = null,

    var url: String? = null,

    var website: String? = null,

    var googleRating: BigDecimal? = null,

    var btRating: BigDecimal? = null,

    var numberOfReviews: Int = 0,

    var priceLevel: String? = null,

    var permanentlyClosed: Boolean? = null,

    var userId: String? = null

) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RestaurantDTO) return false
        if (other.id == null || id == null) return false

        return id == other.id
    }

    override fun hashCode() = id.hashCode()
}
