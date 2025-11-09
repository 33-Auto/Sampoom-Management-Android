package com.sampoom.android.feature.auth.data.mapper

import com.sampoom.android.core.model.UserPosition
import com.sampoom.android.feature.auth.data.remote.dto.GetVendorsResponseDto
import com.sampoom.android.feature.auth.data.remote.dto.LoginResponseDto
import com.sampoom.android.feature.auth.domain.model.Vendor
import com.sampoom.android.feature.user.domain.model.User

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

fun GetVendorsResponseDto.toModel(): Vendor = Vendor(
    id = id,
    vendorCode = vendorCode,
    name = name,
    businessNumber = businessNumber,
    ceoName = ceoName,
    address = address,
    status = status
)