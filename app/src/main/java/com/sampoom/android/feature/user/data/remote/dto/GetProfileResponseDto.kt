package com.sampoom.android.feature.user.data.remote.dto

data class GetProfileResponseDto(
    val userId: Long,
    val userName: String,
    val workspace: String,
    val branch: String,
    val position: String
)
