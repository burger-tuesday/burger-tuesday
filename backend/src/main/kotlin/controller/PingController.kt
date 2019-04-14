package com.grosslicht.burgertuesday.controller

import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1")
class PingController {
    @PostMapping("/ping")
    fun ping(authentication: Authentication): Any {
        return authentication.credentials
    }
}
