package com.sampoom.android.core.util

import com.sampoom.android.core.model.EmployeeStatus

fun employeeStatusToKorean(status: EmployeeStatus?): String = when (status) {
    EmployeeStatus.ACTIVE -> "재직"
    EmployeeStatus.LEAVE -> "휴직"
    EmployeeStatus.RETIRED -> "퇴직"
    else -> "-"
}