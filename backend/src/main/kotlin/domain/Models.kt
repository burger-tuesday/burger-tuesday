package com.grosslicht.burgertuesday.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.grosslicht.burgertuesday.serialization.IdMaskDeserializer
import com.grosslicht.burgertuesday.serialization.IdMaskSerializer
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany

@Entity
data class Restaurant(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonSerialize(using = IdMaskSerializer::class)
    @JsonDeserialize(using = IdMaskDeserializer::class)
    val id: Long = 0,
    @Column(unique = true, nullable = false)
    val placeId: String,
    val name: String,
    val address: String,
    val vicinity: String,
    val url: String,
    val website: String,
    val googleRating: BigDecimal,
    val priceLevel: Int,
    val permanentlyClosed: Boolean = false,
    val createdBy: String,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now(),
    @OneToMany(
        mappedBy = "restaurant",
        orphanRemoval = true,
        cascade = [CascadeType.ALL],
        fetch = FetchType.EAGER
    )
    val visits: MutableSet<Visit> = mutableSetOf()
) {
    fun addVisit(visit: Visit): Restaurant {
        this.visits.add(visit)
        visit.restaurant = this
        return this
    }

    fun removeVisit(visit: Visit): Restaurant {
        this.visits.remove(visit)
        return this
    }

    override fun hashCode(): Int = id.hashCode()
    override fun toString(): String =
        "Restaurant(id=$id, placeId='$placeId', name='$name', address='$address', vicinity='$vicinity', url='$url', website='$website', googleRating=$googleRating, priceLevel='$priceLevel', permanentlyClosed=$permanentlyClosed, createdBy='$createdBy', createdAt=$createdAt, updatedAt=$updatedAt)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Restaurant

        if (id != other.id) return false

        return true
    }
}

@Entity
data class Visit(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonSerialize(using = IdMaskSerializer::class)
    @JsonDeserialize(using = IdMaskDeserializer::class)
    val id: Long = 0,
    val date: LocalDate,
    val sponsored: Boolean,
    @ManyToOne(optional = false)
    @JoinColumn
    @JsonIgnoreProperties("visits")
    var restaurant: Restaurant,
    @OneToMany(mappedBy = "visit", orphanRemoval = true, cascade = [CascadeType.ALL])
    val reviews: MutableSet<Review> = mutableSetOf(),
    val createdBy: String,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now()
) {
    fun addReview(review: Review): Visit {
        this.reviews.add(review)
        review.visit = this
        return this
    }

    fun removeReview(review: Review): Visit {
        this.reviews.remove(review)
        return this
    }

    override fun hashCode(): Int = id.hashCode()
    override fun toString(): String =
        "Visit(id=$id, restaurant=Restaurant(id=${restaurant.id}), date=$date)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Visit

        if (id != other.id) return false

        return true
    }
}

@Entity
data class Review(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonSerialize(using = IdMaskSerializer::class)
    @JsonDeserialize(using = IdMaskDeserializer::class)
    val id: Long = 0,
    @ManyToOne(optional = false)
    @JsonIgnoreProperties("reviews")
    var visit: Visit?,
    val text: String?,
    val taste: Int,
    val likeness: Int,
    val menuDiversity: Int,
    val service: Int,
    val priceLevel: Int,
    val recommended: Boolean,
    var createdBy: String?,
    var createdByName: String?,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now()
) {
    override fun hashCode(): Int = id.hashCode()
    override fun toString(): String =
        "Review(id=$id, visit=${visit?.id}, text=$text, taste=$taste, likeness=$likeness, menuDiversity=$menuDiversity, service=$service, priceLevel=$priceLevel, recommended=$recommended)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Review

        if (id != other.id) return false

        return true
    }
}

data class ReviewEvent(
    val visitDate: LocalDate,
    val visitName: String,
    val author: String,
    val recommended: Boolean
)

data class ToplistEntry(
    val rank: Int,
    val amount: Long,
    val reviewer: String,
    val reviewerName: String
)
