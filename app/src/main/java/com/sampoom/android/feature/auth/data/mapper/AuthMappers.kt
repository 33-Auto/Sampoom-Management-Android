package com.sampoom.android.feature.auth.data.mapper

import com.sampoom.android.feature.auth.data.remote.dto.UserDto
import com.sampoom.android.feature.auth.domain.model.User

fun UserDto.toModel(): User = User(id, name, email, token)