package com.sampoom.android.feature.user.data.remote.dto

data class RefreshResponseDto(
    val accessToken: String,
    val expiresIn: Long,
    val refreshToken: String
)