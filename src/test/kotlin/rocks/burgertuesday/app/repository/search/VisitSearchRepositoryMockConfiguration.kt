package rocks.burgertuesday.app.repository.search

import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Configuration

/**
 * Configure a Mock version of VisitSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
class VisitSearchRepositoryMockConfiguration {

    @MockBean
    private lateinit var mockVisitSearchRepository: VisitSearchRepository
}
