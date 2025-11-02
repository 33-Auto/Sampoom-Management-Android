package com.sampoom.android.feature.auth.data.remote.dto

data class LoginResponseDto(
    val userId: Long,
    val userName: String?,
    val role: String,
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long
)
