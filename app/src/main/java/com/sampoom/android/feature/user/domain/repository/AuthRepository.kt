package com.sampoom.android.feature.user.domain.repository

import com.sampoom.android.feature.user.domain.model.User

interface AuthRepository {
    suspend fun signUp(
        email: String,
        password: String,
        workspace: String,
        branch: String,
        userName: String,
        position: String
    ): User

    suspend fun signIn(email: String, password: String): User
    suspend fun signOut(): Result<Unit>
    suspend fun refreshToken(): Result<User>
    suspend fun clearTokens(): Result<Unit>
    suspend fun isSignedIn(): Boolean
}