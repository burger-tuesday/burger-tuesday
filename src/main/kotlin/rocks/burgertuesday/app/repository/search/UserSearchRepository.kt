package rocks.burgertuesday.app.repository.search

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import rocks.burgertuesday.app.domain.User

/**
 * Spring Data Elasticsearch repository for the User entity.
 */
interface UserSearchRepository : ElasticsearchRepository<User, String>
