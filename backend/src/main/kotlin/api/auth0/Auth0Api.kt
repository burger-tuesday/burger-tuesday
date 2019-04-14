package com.grosslicht.burgertuesday.api.auth0

import com.grosslicht.burgertuesday.api.auth0.dto.UserInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface Auth0Api {
    @GET("userinfo")
    fun userInfo(@Header("Authorization") accessToken: String): Call<UserInfo>
}
