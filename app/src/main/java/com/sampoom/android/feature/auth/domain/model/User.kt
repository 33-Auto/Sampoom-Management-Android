package com.sampoom.android.feature.auth.domain.model

data class User(
    val id: Long,
    val name: String,
    val role: String,
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Int
)
