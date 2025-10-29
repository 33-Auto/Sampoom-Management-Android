package com.sampoom.android.feature.user.domain.model

data class User(
    val userId: Long,
    val userName: String,
    val role: String,
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long,
    val position: String,
    val workspace: String,
    val branch: String
)
