package com.sampoom.android.feature.user.domain.model

import com.sampoom.android.core.model.UserPosition

data class User(
    val userId: Long,
    val userName: String,
    val email: String,
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long,
    val position: UserPosition,
    val workspace: String,
    val branch: String,
    val agencyId: Long,
    val startedAt: String?,
    val endedAt: String?
)