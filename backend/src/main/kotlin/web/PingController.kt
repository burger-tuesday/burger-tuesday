package com.grosslicht.burgertuesday.web

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class PingController {
    @GetMapping("/ping")
    fun ping(): String {
        val context = SecurityContextHolder.getContext().authentication
        return context.toString()
    }
}
