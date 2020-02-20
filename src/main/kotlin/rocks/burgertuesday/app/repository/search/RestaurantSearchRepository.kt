package rocks.burgertuesday.app.repository.search

import rocks.burgertuesday.app.domain.Restaurant
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

/**
 * Spring Data Elasticsearch repository for the [Restaurant] entity.
 */
interface RestaurantSearchRepository : ElasticsearchRepository<Restaurant, Long>
