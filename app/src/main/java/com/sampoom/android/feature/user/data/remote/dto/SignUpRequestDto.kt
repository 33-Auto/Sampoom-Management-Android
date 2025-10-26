package com.sampoom.android.feature.user.data.remote.dto

data class SignUpRequestDto(
    val email: String,
    val password: String,
    val workspace: String,
    val branch: String,
    val userName: String,
    val position: String
)