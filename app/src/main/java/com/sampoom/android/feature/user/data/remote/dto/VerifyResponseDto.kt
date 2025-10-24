package com.sampoom.android.feature.user.data.remote.dto

data class VerifyResponseDto(
    val userId: Long,
    val email: String,
    val userName: String,
    val role: String
)
