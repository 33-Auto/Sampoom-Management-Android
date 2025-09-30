package com.sampoom.android.feature.auth.data.remote.dto

data class UserDto(
    val id: String,
    val name: String,
    val email: String,
    val token: String
)
