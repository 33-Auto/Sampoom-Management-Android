package com.sampoom.android.feature.auth.data.mapper

import com.sampoom.android.core.model.UserPosition
import com.sampoom.android.feature.auth.data.remote.dto.GetProfileResponseDto
import com.sampoom.android.feature.auth.data.remote.dto.GetVendorsResponseDto
import com.sampoom.android.feature.auth.data.remote.dto.LoginResponseDto
import com.sampoom.android.feature.auth.domain.model.User
import com.sampoom.android.feature.auth.domain.model.Vendor

fun LoginResponseDto.toModel(): User = User(
    userId = userId,
    userName = "",
    email = "",
    role = "",
    accessToken = accessToken,
    refreshToken = refreshToken,
    expiresIn = expiresIn,
    position = UserPosition.STAFF,
    workspace = "",
    branch = "",
    agencyId = 0,
    startedAt = null,
    endedAt = null
)

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

fun GetVendorsResponseDto.toModel(): Vendor = Vendor(
    id = id,
    vendorCode = vendorCode,
    name = name,
    businessNumber = businessNumber,
    ceoName = ceoName,
    address = address,
    status = status
)