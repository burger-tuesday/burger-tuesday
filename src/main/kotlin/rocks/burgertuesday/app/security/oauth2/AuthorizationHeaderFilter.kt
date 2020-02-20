package rocks.burgertuesday.app.security.oauth2

import com.netflix.zuul.ZuulFilter
import com.netflix.zuul.context.RequestContext
import rocks.burgertuesday.app.client.TokenRelayRequestInterceptor
import org.springframework.core.Ordered

import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE

class AuthorizationHeaderFilter(private val headerUtil: AuthorizationHeaderUtil) : ZuulFilter() {

    override fun filterType(): String = PRE_TYPE

    override fun filterOrder(): Int = Ordered.LOWEST_PRECEDENCE

    override fun shouldFilter(): Boolean = true

    override fun run(): Any? {
        val ctx = RequestContext.getCurrentContext()
        val authorizationHeader = headerUtil.getAuthorizationHeader()
        ctx.addZuulRequestHeader(TokenRelayRequestInterceptor.AUTHORIZATION, authorizationHeader)
        return null
    }
}
