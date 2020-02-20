package rocks.burgertuesday.app.config

import io.github.jhipster.config.JHipsterConstants
import io.github.jhipster.config.liquibase.AsyncSpringLiquibase
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties
import org.springframework.cache.CacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.core.env.Profiles

import javax.sql.DataSource
import java.util.concurrent.Executor

@Configuration
class LiquibaseConfiguration(private val env: Environment, private val cacheManager: CacheManager) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Bean
    fun liquibase(
        @Qualifier("taskExecutor") executor: Executor,
        dataSource: DataSource,
        liquibaseProperties: LiquibaseProperties
    ) =
        // Use liquibase.integration.spring.SpringLiquibase if you don't want Liquibase to start asynchronously
        AsyncSpringLiquibase(executor, env).apply {
            this.dataSource = dataSource
            changeLog = "classpath:config/liquibase/master.xml"
            contexts = liquibaseProperties.contexts
            defaultSchema = liquibaseProperties.defaultSchema
            isDropFirst = liquibaseProperties.isDropFirst
            setChangeLogParameters(liquibaseProperties.parameters)

            if (env.acceptsProfiles(Profiles.of(JHipsterConstants.SPRING_PROFILE_NO_LIQUIBASE))) {
                setShouldRun(false)
            } else {
                setShouldRun(liquibaseProperties.isEnabled)
                log.debug("Configuring Liquibase")
            }
        }
}
