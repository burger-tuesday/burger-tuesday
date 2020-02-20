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
import java.time.Instant
import java.time.LocalDate

/**
 * A Visit.
 */
@Entity
@Table(name = "visit")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "visit")
class Visit(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    var id: Long? = null,

    @Column(name = "date")
    var date: LocalDate? = null,

    @Column(name = "sponsored")
    var sponsored: Boolean? = null,

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("visits")
    var restaurant: Restaurant? = null,

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("visits")
    var user: User? = null,

    @OneToMany(mappedBy = "visit")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    var reviews: MutableSet<Review> = mutableSetOf(),

    createdBy: String? = null,
    createdDate: Instant? = Instant.now(),
    lastModifiedBy: String? = null,
    lastModifiedDate: Instant? = Instant.now()
) : AbstractAuditingEntity(createdBy, createdDate, lastModifiedBy, lastModifiedDate), Serializable {

    fun addReviews(review: Review): Visit {
        this.reviews.add(review)
        review.visit = this
        return this
    }

    fun removeReviews(review: Review): Visit {
        this.reviews.remove(review)
        review.visit = null
        return this
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Visit) return false
        if (other.id == null || id == null) return false

        return id == other.id
    }

    override fun hashCode() = 31

    override fun toString() = "Visit{" +
        "id=$id" +
        ", date='$date'" +
        ", sponsored='$sponsored'" +
        "}"

    companion object {
        private const val serialVersionUID = 1L
    }
}
