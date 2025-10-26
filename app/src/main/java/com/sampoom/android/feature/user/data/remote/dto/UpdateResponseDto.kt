package com.sampoom.android.feature.user.data.remote.dto

data class UpdateResponseDto(
    val userId: Long,
    val email: String,
    val userName: String,
    val position: String,
    val workspace: String,
    val branch: String
)
