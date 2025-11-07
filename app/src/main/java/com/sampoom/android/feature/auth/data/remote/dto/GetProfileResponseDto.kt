package com.sampoom.android.feature.auth.data.remote.dto

data class GetProfileResponseDto(
    val userId: Long,
    val userName: String,
    val email: String,
    val role: String,
    val position: String,
    val workspace: String,
    val branch: String,
    val organizationId: Long,
    val startedAt: String,
    val endedAt: String?
)
