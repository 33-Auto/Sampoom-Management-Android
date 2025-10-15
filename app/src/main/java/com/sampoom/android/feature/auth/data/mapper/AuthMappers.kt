package com.sampoom.android.feature.auth.data.mapper

import com.sampoom.android.feature.auth.data.remote.dto.LoginResponseDto
import com.sampoom.android.feature.auth.domain.model.User

fun LoginResponseDto.toModel(): User = User(userId, userName, role, accessToken, refreshToken, expiresIn)