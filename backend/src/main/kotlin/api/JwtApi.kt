package com.grosslicht.burgertuesday.api

import com.grosslicht.burgertuesday.api.dto.AuthToken
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

@Validated
@RequestMapping("/v1")
interface JwtApi {
    @PostMapping("/jwt/authToken")
    fun authToken(): ResponseEntity<AuthToken> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }
}
