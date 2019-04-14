package com.grosslicht.burgertuesday.api.auth0.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.google.gson.annotations.SerializedName

@JsonIgnoreProperties(ignoreUnknown = true)
data class UserInfo(
    @SerializedName("email_verified") val emailVerified: Boolean = false,
    @SerializedName("given_name") val givenName: String = "",
    @SerializedName("family_name") val familyName: String = "",
    val email: String = "",
    val name: String = "",
    val picture: String = "",
    val nickname: String = "",
    @SerializedName("created_at") val createdAt: String = "",
    @SerializedName("updated_at") val updatedAt: String = "",
    val sub: String = ""
)
