package rocks.burgertuesday.app.security

import java.util.Optional
import org.springframework.data.domain.AuditorAware
import org.springframework.stereotype.Component
import rocks.burgertuesday.app.config.SYSTEM_ACCOUNT

/**
 * Implementation of [AuditorAware] based on Spring Security.
 */
@Component
class SpringSecurityAuditorAware : AuditorAware<String> {
    override fun getCurrentAuditor(): Optional<String> = Optional.of(getCurrentUserLogin().orElse(SYSTEM_ACCOUNT))
}
