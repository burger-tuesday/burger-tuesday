package com.grosslicht.burgertuesday.controller

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class PingController {
    @GetMapping("/ping")
    fun ping(): String {
        val context = SecurityContextHolder.getContext().authentication
        println(context.authorities)
        println(context.details.toString())
        println(context.principal.toString())
        return context.toString()
    }
}
