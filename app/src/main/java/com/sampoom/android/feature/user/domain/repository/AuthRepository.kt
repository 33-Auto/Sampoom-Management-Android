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
    suspend fun signOut()
    suspend fun refreshToken(): Result<User>
    suspend fun clearTokens()
    suspend fun isSignedIn(): Boolean
}