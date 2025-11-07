package com.sampoom.android.feature.auth.data.remote.dto

data class LoginResponseDto(
    val userId: Long,
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long
)
