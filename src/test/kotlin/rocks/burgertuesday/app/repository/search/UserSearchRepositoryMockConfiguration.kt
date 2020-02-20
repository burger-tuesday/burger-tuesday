package rocks.burgertuesday.app.repository.search

import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Configuration

/**
 * Configure a Mock version of [UserSearchRepository] to test the
 * application without starting Elasticsearch.
 */
@Configuration
class UserSearchRepositoryMockConfiguration {

    @MockBean
    private lateinit var mockUserSearchRepository: UserSearchRepository
}
