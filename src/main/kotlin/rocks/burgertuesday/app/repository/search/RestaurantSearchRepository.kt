package rocks.burgertuesday.app.repository.search

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import rocks.burgertuesday.app.domain.Restaurant

/**
 * Spring Data Elasticsearch repository for the [Restaurant] entity.
 */
interface RestaurantSearchRepository : ElasticsearchRepository<Restaurant, Long>
