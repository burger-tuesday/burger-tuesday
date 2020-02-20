package rocks.burgertuesday.app.web.rest.errors

import org.springframework.dao.ConcurrencyFailureException
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

import javax.validation.Valid
import javax.validation.constraints.NotNull

@RestController
class ExceptionTranslatorTestController {

    @GetMapping("/test/concurrency-failure")
    fun concurrencyFailure(): Unit = throw ConcurrencyFailureException("test concurrency failure")

    @PostMapping("/test/method-argument")
    fun methodArgument(@Valid @RequestBody testDTO: TestDTO) = Unit

    @GetMapping("/test/missing-servlet-request-part")
    fun missingServletRequestPartException(@RequestPart part: String) = Unit

    @GetMapping("/test/missing-servlet-request-parameter")
    fun missingServletRequestParameterException(@RequestParam param: String) = Unit

    @GetMapping("/test/access-denied")
    fun accessdenied(): Unit = throw AccessDeniedException("test access denied!")

    @GetMapping("/test/unauthorized")
    fun unauthorized(): Unit = throw BadCredentialsException("test authentication failed!")

    @GetMapping("/test/response-status")
    fun exceptionWithResponseStatus(): Unit = throw TestResponseStatusException()

    @GetMapping("/test/internal-server-error")
    fun internalServerError(): Unit = throw RuntimeException()

    class TestDTO {
        @field:NotNull
        var test: String? = null
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "test response status")
    class TestResponseStatusException : RuntimeException()
}
