package com.sampoom.android.feature.auth.domain.model

enum class UserRole {
    STAFF,               // 사원
    SENIOR_STAFF,        // 주임
    ASSISTANT_MANAGER,   // 대리
    MANAGER,             // 과장
    DEPUTY_GENERAL_MANAGER, // 차장
    GENERAL_MANAGER,     // 부장
    DIRECTOR,            // 이사
    VICE_PRESIDENT,      // 부사장
    PRESIDENT,           // 사장
    CHAIRMAN             // 회장
}

data class User(
    val userId: Long,
    val userName: String,
    val email: String,
    val role: UserRole,
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long,
    val position: String,
    val workspace: String,
    val branch: String,
    val agencyId: Long,
    val startedAt: String?,
    val endedAt: String?
)
