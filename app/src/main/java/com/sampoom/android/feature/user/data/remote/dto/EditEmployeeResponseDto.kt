package com.sampoom.android.feature.user.data.remote.dto

data class EditEmployeeResponseDto(
    val userId: Long,
    val userName: String,
    val role: String,
    val position: String
)
