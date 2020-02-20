package rocks.burgertuesday.app.repository.search

import rocks.burgertuesday.app.domain.Review
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

/**
 * Spring Data Elasticsearch repository for the [Review] entity.
 */
interface ReviewSearchRepository : ElasticsearchRepository<Review, Long>
