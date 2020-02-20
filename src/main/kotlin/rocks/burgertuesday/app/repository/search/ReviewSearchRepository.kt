package rocks.burgertuesday.app.repository.search

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import rocks.burgertuesday.app.domain.Review

/**
 * Spring Data Elasticsearch repository for the [Review] entity.
 */
interface ReviewSearchRepository : ElasticsearchRepository<Review, Long>
