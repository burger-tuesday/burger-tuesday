package rocks.burgertuesday.app.service.mapper

import rocks.burgertuesday.app.domain.Review
import rocks.burgertuesday.app.service.dto.ReviewDTO

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

/**
 * Mapper for the entity [Review] and its DTO [ReviewDTO].
 */
@Mapper(componentModel = "spring", uses = [VisitMapper::class, UserMapper::class])
interface ReviewMapper :
    EntityMapper<ReviewDTO, Review> {
    @Mappings(
        Mapping(source = "visit.id", target = "visitId"),
        Mapping(source = "user.id", target = "userId")
    )
    override fun toDto(entity: Review): ReviewDTO

    @Mappings(
        Mapping(source = "visitId", target = "visit"),
        Mapping(source = "userId", target = "user")
    )
    override fun toEntity(dto: ReviewDTO): Review

    fun fromId(id: Long?) = id?.let { Review(it) }
}
