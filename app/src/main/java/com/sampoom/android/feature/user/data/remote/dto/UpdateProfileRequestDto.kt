package com.sampoom.android.feature.user.data.remote.dto

data class UpdateProfileRequestDto(
    val userName: String,
    val position: String,
    val workspace: String,
    val branch: String
)
