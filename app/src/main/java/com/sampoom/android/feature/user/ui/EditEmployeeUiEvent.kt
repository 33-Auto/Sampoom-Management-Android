package com.sampoom.android.feature.user.ui

import com.sampoom.android.core.model.UserPosition
import com.sampoom.android.feature.user.domain.model.Employee

interface EditEmployeeUiEvent {
    data class Initialize(val employee: Employee) : EditEmployeeUiEvent
    data class EditEmployee(val position: UserPosition) : EditEmployeeUiEvent
    object Dismiss : EditEmployeeUiEvent
}