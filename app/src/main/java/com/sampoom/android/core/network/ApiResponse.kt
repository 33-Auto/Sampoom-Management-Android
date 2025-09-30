package com.sampoom.android.core.network

data class ApiResponse<T>(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: T
)