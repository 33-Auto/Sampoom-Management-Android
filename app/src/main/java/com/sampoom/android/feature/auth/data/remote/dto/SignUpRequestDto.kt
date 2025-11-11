package com.sampoom.android.feature.auth.data.remote.dto

data class SignUpRequestDto(
    val email: String,
    val password: String,
    val role: String,
    val branch: String,
    val userName: String,
    val position: String
)