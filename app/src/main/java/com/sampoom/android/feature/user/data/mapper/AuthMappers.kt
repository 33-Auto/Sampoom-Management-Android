package com.sampoom.android.feature.user.data.mapper

import com.sampoom.android.feature.user.data.remote.dto.GetProfileResponseDto
import com.sampoom.android.feature.user.data.remote.dto.LoginResponseDto
import com.sampoom.android.feature.user.domain.model.User

fun LoginResponseDto.toModel(): User = User(
    userId = userId,
    userName = "",
    role = role,
    accessToken = accessToken,
    refreshToken = refreshToken,
    expiresIn = expiresIn,
    position = "",
    workspace = "",
    branch = ""
)

fun GetProfileResponseDto.toModel(): User = User(
    userId = userId,
    userName = userName,
    role = "",
    accessToken = "",
    refreshToken = "",
    expiresIn = 0L,
    position = position,
    workspace = workspace,
    branch = branch
)

fun User.mergeWith(profile: User): User = this.copy(
    userName = profile.userName,
    position = profile.position,
    workspace = profile.workspace,
    branch = profile.branch
)