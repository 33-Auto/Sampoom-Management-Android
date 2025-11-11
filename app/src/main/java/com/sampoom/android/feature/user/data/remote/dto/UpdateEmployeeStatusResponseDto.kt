package com.sampoom.android.feature.user.data.remote.dto

data class UpdateEmployeeStatusResponseDto(
    val userId: Long,
    val userName: String,
    val role: String,
    val employeeStatus: String
)
