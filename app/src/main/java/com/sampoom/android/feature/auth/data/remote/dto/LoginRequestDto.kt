package com.sampoom.android.feature.auth.data.remote.dto

data class LoginRequestDto(
    val workspace: String,
    val email: String,
    val password: String
)