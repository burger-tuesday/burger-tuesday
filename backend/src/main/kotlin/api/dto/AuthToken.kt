package com.grosslicht.burgertuesday.api.dto

import com.fasterxml.jackson.annotation.JsonProperty

/**
 *
 * @param token
 */
data class AuthToken(
    @JsonProperty("token") val token: String? = null
)
