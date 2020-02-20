package rocks.burgertuesday.app.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.SequenceGenerator
import javax.persistence.Table
import javax.validation.constraints.NotNull

import org.springframework.data.elasticsearch.annotations.FieldType
import java.io.Serializable
import java.math.BigDecimal
import java.time.Instant

/**
 * A Restaurant.
 */
@Entity
@Table(name = "restaurant")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "restaurant")
class Restaurant(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    var id: Long? = null,

    @Column(name = "place_id")
    var placeId: String? = null,

    @Column(name = "name")
    var name: String? = null,

    @Column(name = "address")
    var address: String? = null,

    @Column(name = "vicinity")
    var vicinity: String? = null,

    @Column(name = "url")
    var url: String? = null,

    @Column(name = "website")
    var website: String? = null,

    @Column(name = "google_rating", precision = 21, scale = 2)
    var googleRating: BigDecimal? = null,

    @Column(name = "bt_rating", precision = 21, scale = 2)
    var btRating: BigDecimal? = null,

    @Column(name = "num_reviews")
    var numberOfReviews: Int? = null,

    @Column(name = "price_level")
    var priceLevel: String? = null,

    @Column(name = "permanently_closed")
    var permanentlyClosed: Boolean? = null,

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("restaurants")
    var user: User? = null,

    @OneToMany(mappedBy = "restaurant")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    var visits: MutableSet<Visit> = mutableSetOf(),

    createdBy: String? = null,
    createdDate: Instant? = Instant.now(),
    lastModifiedBy: String? = null,
    lastModifiedDate: Instant? = Instant.now()
) : AbstractAuditingEntity(createdBy, createdDate, lastModifiedBy, lastModifiedDate), Serializable {

    fun addVisits(visit: Visit): Restaurant {
        this.visits.add(visit)
        visit.restaurant = this
        return this
    }

    fun removeVisits(visit: Visit): Restaurant {
        this.visits.remove(visit)
        visit.restaurant = null
        return this
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Restaurant) return false
        if (other.id == null || id == null) return false

        return id == other.id
    }

    override fun hashCode() = 31

    override fun toString() = "Restaurant{" +
        "id=$id" +
        ", placeId='$placeId'" +
        ", name='$name'" +
        ", address='$address'" +
        ", vicinity='$vicinity'" +
        ", url='$url'" +
        ", website='$website'" +
        ", googleRating=$googleRating" +
        ", priceLevel='$priceLevel'" +
        ", permanentlyClosed='$permanentlyClosed'" +
        "}"

    companion object {
        private const val serialVersionUID = 1L
    }
}
