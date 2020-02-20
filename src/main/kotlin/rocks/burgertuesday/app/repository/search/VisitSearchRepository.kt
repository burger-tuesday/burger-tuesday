package rocks.burgertuesday.app.repository.search

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import rocks.burgertuesday.app.domain.Visit

/**
 * Spring Data Elasticsearch repository for the [Visit] entity.
 */
interface VisitSearchRepository : ElasticsearchRepository<Visit, Long>
