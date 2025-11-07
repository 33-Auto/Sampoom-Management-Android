package com.sampoom.android.feature.user.domain.model

import com.sampoom.android.core.model.UserPosition

data class Employee(
    val userId: Long,
    val email: String,
    val role: String,
    val userName: String,
    val workspace: String,
    val organizationId: Long,
    val branch: String,
    val position: UserPosition,
    val startedAt: String?,
    val endedAt: String?
)
