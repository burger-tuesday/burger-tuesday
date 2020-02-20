package rocks.burgertuesday.app.repository.search

import rocks.burgertuesday.app.domain.Visit
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

/**
 * Spring Data Elasticsearch repository for the [Visit] entity.
 */
interface VisitSearchRepository : ElasticsearchRepository<Visit, Long>
