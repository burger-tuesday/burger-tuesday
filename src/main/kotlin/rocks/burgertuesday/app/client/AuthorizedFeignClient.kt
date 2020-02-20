package rocks.burgertuesday.app.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.cloud.openfeign.FeignClientsConfiguration
import org.springframework.core.annotation.AliasFor

import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@MustBeDocumented
@FeignClient
annotation class AuthorizedFeignClient(
    @get:AliasFor(annotation = FeignClient::class, attribute = "name")
    val name: String = "",

    /**
     * A custom `@Configuration` for the feign client.
     *
     * Can contain override `@Bean` definition for the pieces that
     * make up the client, for instance [feign.codec.Decoder],
     * [feign.codec.Encoder], [feign.Contract].
     *
     * @return the custom `@Configuration` for the feign client.
     * @see FeignClientsConfiguration for the defaults.
     */
    @get:AliasFor(annotation = FeignClient::class, attribute = "configuration")
    val configuration: Array<KClass<*>> = [OAuth2InterceptedFeignConfiguration::class],

    /**
     * An absolute URL or resolvable hostname (the protocol is optional).
     * @return the URL.
     */
    val url: String = "",

    /**
     * Whether 404s should be decoded instead of throwing `FeignException`s.
     * @return true if 404s will be decoded; false otherwise.
     */
    val decode404: Boolean = false,

    /**
     * Fallback class for the specified Feign client interface. The fallback class must
     * implement the interface annotated by this annotation and be a valid Spring bean.
     * @return the fallback class for the specified Feign client interface.
     */
    val fallback: KClass<*> = Unit::class,

    /**
     * Path prefix to be used by all method-level mappings. Can be used with or without `@RibbonClient`.
     * @return the path prefix to be used by all method-level mappings.
     */
    val path: String = ""
)
