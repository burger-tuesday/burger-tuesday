package com.grosslicht.burgertuesday.aop

import com.grosslicht.burgertuesday.config.BurgerTuesdayConstants
import mu.KotlinLogging
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.AfterThrowing
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.springframework.core.env.Environment
import java.util.Arrays

private val logger = KotlinLogging.logger {}

/**
 * Aspect for logging execution of service and repository Spring components.
 *
 * By default, it only runs with the "dev" profile.
 */
@Aspect
class LoggingAspect(private val env: Environment) {

    /**
     * Pointcut that matches all repositories, services and Web REST endpoints.
     */
    @Pointcut(
        "within(@org.springframework.stereotype.Repository *)" +
                " || within(@org.springframework.stereotype.Service *)" +
                " || within(@org.springframework.web.bind.annotation.RestController *)"
    )
    fun springBeanPointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    /**
     * Pointcut that matches all Spring beans in the application's main packages.
     */
    @Pointcut(
        "within(com.grosslicht.burgertuesday.repository..*)" +
                " || within(com.grosslicht.burgertuesday.service..*)" +
                " || within(com.grosslicht.burgertuesday.web..*)"
    )
    fun applicationPackagePointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    /**
     * Advice that logs methods throwing exceptions.
     *
     * @param joinPoint join point for advice
     * @param e exception
     */
    @AfterThrowing(
        pointcut = "applicationPackagePointcut() && springBeanPointcut()",
        throwing = "e"
    )
    fun logAfterThrowing(joinPoint: JoinPoint, e: Throwable) {
        if (env.acceptsProfiles(BurgerTuesdayConstants.DEVELOPMENT_SPRING_PROFILE)) {
            logger.error(
                "Exception in {}.{}() with cause = \'{}\' and exception = \'{}\'",
                joinPoint.signature.declaringTypeName,
                joinPoint.signature.name,
                if (e.cause != null) e.cause else "NULL",
                e.message,
                e
            )
        } else {
            logger.error(
                "Exception in {}.{}() with cause = {}", joinPoint.signature.declaringTypeName,
                joinPoint.signature.name, if (e.cause != null) e.cause else "NULL"
            )
        }
    }

    /**
     * Advice that logs when a method is entered and exited.
     *
     * @param joinPoint join point for advice
     * @return result
     * @throws Throwable throws IllegalArgumentException
     */
    @Around("applicationPackagePointcut() && springBeanPointcut()")
    @Throws(Throwable::class)
    fun logAround(joinPoint: ProceedingJoinPoint): Any {
        if (logger.isDebugEnabled) {
            logger.debug(
                "Enter: {}.{}() with argument[s] = {}", joinPoint.signature.declaringTypeName,
                joinPoint.signature.name, Arrays.toString(joinPoint.args)
            )
        }
        try {
            val result = joinPoint.proceed()
            if (logger.isDebugEnabled) {
                logger.debug(
                    "Exit: {}.{}() with result = {}", joinPoint.signature.declaringTypeName,
                    joinPoint.signature.name, result
                )
            }
            return result
        } catch (e: IllegalArgumentException) {
            logger.error(
                "Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.args),
                joinPoint.signature.declaringTypeName, joinPoint.signature.name
            )

            throw e
        }
    }
}
