package rocks.burgertuesday.app.repository.search

import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Configuration

/**
 * Configure a Mock version of RestaurantSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
class RestaurantSearchRepositoryMockConfiguration {

    @MockBean
    private lateinit var mockRestaurantSearchRepository: RestaurantSearchRepository
}
