package rocks.burgertuesday.app.config

import rocks.burgertuesday.app.security.ANONYMOUS

import io.github.jhipster.config.JHipsterProperties

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.http.server.ServletServerHttpRequest
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer
import org.springframework.web.socket.server.HandshakeInterceptor
import org.springframework.web.socket.server.support.DefaultHandshakeHandler

import java.security.Principal
import java.util.Optional

@Configuration
@EnableWebSocketMessageBroker
class WebsocketConfiguration(private val jHipsterProperties: JHipsterProperties) : WebSocketMessageBrokerConfigurer {

    override fun configureMessageBroker(config: MessageBrokerRegistry?) {
        config!!.enableSimpleBroker("/topic")
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry?) {
        val allowedOrigins =
            Optional.ofNullable(jHipsterProperties.cors.allowedOrigins)
                .map { origins -> origins.toTypedArray() }
                .orElse(arrayOfNulls(0))

        registry!!.addEndpoint("/websocket/tracker")
            .setHandshakeHandler(defaultHandshakeHandler())
            .setAllowedOrigins(*allowedOrigins)
            .withSockJS()
            .setInterceptors(httpSessionHandshakeInterceptor())
    }

    @Bean
    fun httpSessionHandshakeInterceptor(): HandshakeInterceptor {
        return object : HandshakeInterceptor {

            @Throws(Exception::class)
            override fun beforeHandshake(
                request: ServerHttpRequest,
                response: ServerHttpResponse,
                wsHandler: WebSocketHandler,
                attributes: MutableMap<String, Any>
            ): Boolean {
                if (request is ServletServerHttpRequest) {
                    attributes[IP_ADDRESS] = request.remoteAddress
                }
                return true
            }

            override fun afterHandshake(
                request: ServerHttpRequest,
                response: ServerHttpResponse,
                wsHandler: WebSocketHandler,
                exception: Exception?
            ) {
            }
        }
    }

    private fun defaultHandshakeHandler(): DefaultHandshakeHandler {
        return object : DefaultHandshakeHandler() {
            override fun determineUser(
                request: ServerHttpRequest,
                wsHandler: WebSocketHandler?,
                attributes: Map<String, Any>?
            ): Principal? {
                var principal: Principal? = request.principal
                if (principal == null) {
                    val authorities = mutableListOf<SimpleGrantedAuthority>()
                    authorities.add(SimpleGrantedAuthority(ANONYMOUS))
                    principal = AnonymousAuthenticationToken("WebsocketConfiguration", "anonymous", authorities)
                }
                return principal
            }
        }
    }

    companion object {

        const val IP_ADDRESS = "IP_ADDRESS"
    }
}
