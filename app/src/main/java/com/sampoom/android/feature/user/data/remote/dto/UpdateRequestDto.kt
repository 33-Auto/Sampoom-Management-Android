package com.sampoom.android.feature.user.data.remote.dto

data class UpdateRequestDto(
    val userName: String,
    val position: String,
    val workspace: String,
    val branch: String
)
