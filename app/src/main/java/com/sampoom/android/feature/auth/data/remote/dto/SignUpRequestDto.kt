package com.sampoom.android.feature.auth.data.remote.dto

data class SignUpRequestDto(
    val name: String,
    val workspace: String,
    val branch: String,
    val position: String,
    val email: String,
    val password: String
)