package com.sampoom.android.feature.user.data.mapper

import com.sampoom.android.core.model.EmployeeStatus
import com.sampoom.android.core.model.UserPosition
import com.sampoom.android.feature.user.data.remote.dto.EditEmployeeResponseDto
import com.sampoom.android.feature.user.data.remote.dto.EmployeeDto
import com.sampoom.android.feature.user.data.remote.dto.GetProfileResponseDto
import com.sampoom.android.feature.user.data.remote.dto.UpdateEmployeeStatusResponseDto
import com.sampoom.android.feature.user.data.remote.dto.UpdateProfileResponseDto
import com.sampoom.android.feature.user.domain.model.Employee
import com.sampoom.android.feature.user.domain.model.User

fun GetProfileResponseDto.toModel(): User = User(
    userId = userId,
    userName = userName,
    email = email,
    accessToken = "",
    refreshToken = "",
    expiresIn = 0L,
    position = position.toUserPosition(),
    workspace = workspace,
    branch = branch,
    agencyId = organizationId,
    startedAt = startedAt,
    endedAt = endedAt
)

private fun String.toUserPosition(): UserPosition = try {
    UserPosition.valueOf(this.uppercase())
} catch (_: IllegalArgumentException) {
    UserPosition.STAFF
}

fun UpdateProfileResponseDto.toModel(): User = User(
    userId = userId,
    userName = userName,
    email = "",
    accessToken = "",
    refreshToken = "",
    expiresIn = 0L,
    position = UserPosition.STAFF,
    workspace = "",
    branch = "",
    agencyId = 0,
    startedAt = null,
    endedAt = null
)

fun EditEmployeeResponseDto.toModel(): Employee = Employee(
    userId = userId,
    email = "",
    userName = userName,
    workspace = "",
    organizationId = 0,
    branch = "",
    position = position.toUserPosition(),
    status = EmployeeStatus.ACTIVE,
    createdAt = null,
    startedAt = null,
    endedAt = null,
    deletedAt = null
)

fun UpdateEmployeeStatusResponseDto.toModel(): Employee = Employee(
    userId = userId,
    email = "",
    userName = userName,
    workspace = "",
    organizationId = 0,
    branch = "",
    position = UserPosition.STAFF,
    status = employeeStatus.toEmployeeStatus(),
    createdAt = null,
    startedAt = null,
    endedAt = null,
    deletedAt = null
)

fun EmployeeDto.toModel(): Employee = Employee(
    userId,
    email,
    userName,
    workspace,
    organizationId,
    branch,
    position,
    status ?: EmployeeStatus.ACTIVE,
    createdAt,
    startedAt,
    endedAt,
    deletedAt
)

private fun String.toEmployeeStatus(): EmployeeStatus = try {
    EmployeeStatus.valueOf(this.uppercase())
} catch (_: IllegalArgumentException) {
    EmployeeStatus.ACTIVE
}