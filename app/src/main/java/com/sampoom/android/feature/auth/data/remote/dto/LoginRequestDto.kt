package com.sampoom.android.feature.auth.data.remote.dto

data class LoginRequestDto(
    val role: String,
    val email: String,
    val password: String
)