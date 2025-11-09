package com.sampoom.android.feature.user.ui

import com.sampoom.android.core.model.EmployeeStatus
import com.sampoom.android.feature.user.domain.model.Employee

interface UpdateEmployeeStatusUiEvent {
    data class Initialize(val employee: Employee) : UpdateEmployeeStatusUiEvent
    data class EditEmployeeStatus(val employeeStatus: EmployeeStatus) : UpdateEmployeeStatusUiEvent
    object Dismiss : UpdateEmployeeStatusUiEvent
}