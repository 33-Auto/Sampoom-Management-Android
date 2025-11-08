package com.sampoom.android.feature.user.ui

import com.sampoom.android.feature.user.domain.model.Employee

data class UpdateEmployeeStatusUiState(
    val employee: Employee? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)