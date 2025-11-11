package com.sampoom.android.feature.user.domain.model

import com.sampoom.android.core.model.EmployeeStatus
import com.sampoom.android.core.model.UserPosition

data class Employee(
    val userId: Long,
    val email: String,
    val userName: String,
    val workspace: String,
    val organizationId: Long,
    val branch: String,
    val position: UserPosition,
    val status: EmployeeStatus,
    val createdAt: String?,
    val startedAt: String?,
    val endedAt: String?,
    val deletedAt: String?
)
