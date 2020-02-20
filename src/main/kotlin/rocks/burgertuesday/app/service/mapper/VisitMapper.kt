package rocks.burgertuesday.app.service.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import rocks.burgertuesday.app.domain.Visit
import rocks.burgertuesday.app.service.dto.VisitDTO

/**
 * Mapper for the entity [Visit] and its DTO [VisitDTO].
 */
@Mapper(componentModel = "spring", uses = [RestaurantMapper::class, UserMapper::class])
interface VisitMapper :
    EntityMapper<VisitDTO, Visit> {
    @Mappings(
        Mapping(source = "restaurant.id", target = "restaurantId"),
        Mapping(source = "user.id", target = "userId")
    )
    override fun toDto(entity: Visit): VisitDTO

    @Mappings(
        Mapping(source = "restaurantId", target = "restaurant"),
        Mapping(source = "userId", target = "user"),
        Mapping(target = "reviews", ignore = true),
        Mapping(target = "removeReviews", ignore = true)
    )
    override fun toEntity(dto: VisitDTO): Visit

    fun fromId(id: Long?) = id?.let { Visit(it) }
}
