package rocks.burgertuesday.app.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable
import java.time.Instant
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.SequenceGenerator
import javax.persistence.Table
import javax.validation.constraints.NotNull
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy
import org.springframework.data.elasticsearch.annotations.FieldType

/**
 * A Review.
 */
@Entity
@Table(name = "review")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "review")
class Review(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    var id: Long? = null,

    @Column(name = "review")
    var review: String? = null,

    @Column(name = "taste")
    var taste: Int? = null,

    @Column(name = "likeness")
    var likeness: Int? = null,

    @Column(name = "menu_diversity")
    var menuDiversity: Int? = null,

    @Column(name = "service")
    var service: Int? = null,

    @Column(name = "price_level")
    var priceLevel: Int? = null,

    @Column(name = "recommended")
    var recommended: Boolean? = null,

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("reviews")
    var visit: Visit? = null,

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("reviews")
    var user: User? = null,

    createdBy: String? = null,
    createdDate: Instant? = Instant.now(),
    lastModifiedBy: String? = null,
    lastModifiedDate: Instant? = Instant.now()
) : AbstractAuditingEntity(createdBy, createdDate, lastModifiedBy, lastModifiedDate), Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Review) return false
        if (other.id == null || id == null) return false

        return id == other.id
    }

    override fun hashCode() = 31

    override fun toString() = "Review{" +
        "id=$id" +
        ", review='$review'" +
        ", taste=$taste" +
        ", likeness=$likeness" +
        ", menuDiversity=$menuDiversity" +
        ", service=$service" +
        ", priceLevel=$priceLevel" +
        ", recommended='$recommended'" +
        "}"

    companion object {
        private const val serialVersionUID = 1L
    }
}
