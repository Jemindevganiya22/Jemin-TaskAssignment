package com.example.testDemo.model

import com.squareup.moshi.Json

data class User(
    @Json(name = "user_name")
    val username: String? = null,
    @Json(name = "email")
    val email: String? = null,
    @Json(name = "password")
    val password: String? = null,
    @Json(name = "mobile_number")
    val mno: String? = null,
    @Json(name = "gender")
    val gender: String? = null,
    @Json(name = "token")
    val token: String? = null
)