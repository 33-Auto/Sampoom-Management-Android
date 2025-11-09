package com.sampoom.android.feature.user.ui

import com.sampoom.android.feature.user.domain.model.Employee

interface EmployeeListUiEvent {
    object LoadEmployeeList : EmployeeListUiEvent
    object RetryEmployeeList : EmployeeListUiEvent
    data class ShowEditBottomSheet(val employee: Employee) : EmployeeListUiEvent
    data class ShowStatusBottomSheet(val employee: Employee) : EmployeeListUiEvent
    object DismissBottomSheet : EmployeeListUiEvent
}