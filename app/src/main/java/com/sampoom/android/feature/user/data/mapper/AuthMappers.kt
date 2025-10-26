package com.sampoom.android.feature.user.data.mapper

import com.sampoom.android.feature.user.data.remote.dto.LoginResponseDto
import com.sampoom.android.feature.user.domain.model.User

fun LoginResponseDto.toModel(): User = User(userId, userName, role, accessToken, refreshToken, expiresIn)