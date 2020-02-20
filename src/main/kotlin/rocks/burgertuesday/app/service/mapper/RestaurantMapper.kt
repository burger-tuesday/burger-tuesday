package rocks.burgertuesday.app.service.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import rocks.burgertuesday.app.domain.Restaurant
import rocks.burgertuesday.app.service.dto.RestaurantDTO

/**
 * Mapper for the entity [Restaurant] and its DTO [RestaurantDTO].
 */
@Mapper(componentModel = "spring", uses = [UserMapper::class])
interface RestaurantMapper :
    EntityMapper<RestaurantDTO, Restaurant> {
    @Mappings(
        Mapping(source = "user.id", target = "userId")
    )
    override fun toDto(entity: Restaurant): RestaurantDTO

    @Mappings(
        Mapping(source = "userId", target = "user"),
        Mapping(target = "visits", ignore = true),
        Mapping(target = "removeVisits", ignore = true)
    )
    override fun toEntity(dto: RestaurantDTO): Restaurant

    fun fromId(id: Long?) = id?.let { Restaurant(it) }
}
