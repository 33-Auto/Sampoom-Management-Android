package com.sampoom.android.feature.auth.data.remote.dto

data class UpdateProfileRequestDto(
    val userName: String,
    val position: String,
    val workspace: String,
    val branch: String
)
