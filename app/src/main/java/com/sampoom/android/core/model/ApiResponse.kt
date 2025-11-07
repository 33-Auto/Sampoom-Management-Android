package com.sampoom.android.core.model

data class ApiResponse<T>(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: T
)

data class ApiSuccessResponse(
    val status: Int,
    val success: Boolean,
    val message: String
)

data class ApiErrorResponse(
    val code: Int? = null,
    val message: String? = null
)