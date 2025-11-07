package com.sampoom.android.feature.user.data.mapper

import com.sampoom.android.core.model.UserPosition
import com.sampoom.android.feature.user.data.remote.dto.EditEmployeeResponseDto
import com.sampoom.android.feature.user.data.remote.dto.EmployeeDto
import com.sampoom.android.feature.user.data.remote.dto.GetProfileResponseDto
import com.sampoom.android.feature.user.data.remote.dto.UpdateProfileResponseDto
import com.sampoom.android.feature.user.domain.model.Employee
import com.sampoom.android.feature.user.domain.model.User

fun GetProfileResponseDto.toModel(): User = User(
    userId = userId,
    userName = userName,
    email = email,
    role = role,
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
    role = "",
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
    role = "",
    userName = userName,
    workspace = workspace,
    organizationId = 0,
    branch = "",
    position = position.toUserPosition(),
    startedAt = null,
    endedAt = null
)

fun EmployeeDto.toModel(): Employee = Employee(
    userId,
    email,
    role,
    userName,
    workspace,
    organizationId,
    branch,
    position,
    startedAt,
    endedAt
)