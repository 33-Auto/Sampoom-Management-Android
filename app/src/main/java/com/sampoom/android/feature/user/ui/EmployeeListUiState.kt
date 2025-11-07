package com.sampoom.android.feature.user.ui

import com.sampoom.android.feature.user.domain.model.Employee

data class EmployeeListUiState(
    val employeeList: List<Employee> = emptyList(),
    val employeeLoading: Boolean = false,
    val employeeError: String? = null,
    val selectedEmployee: Employee? = null
)
