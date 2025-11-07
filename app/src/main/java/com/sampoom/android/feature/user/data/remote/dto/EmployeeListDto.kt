package com.sampoom.android.feature.user.data.remote.dto

data class EmployeeListDto(
    val users: List<EmployeeDto>,
    val meta: EmployeeMetaDto
)

data class EmployeeMetaDto(
    val currentPage: Int,
    val totalPages: Int,
    val totalElements: Int,
    val size: Int,
    val hasNext: Boolean,
    val hasPrevious: Boolean
)