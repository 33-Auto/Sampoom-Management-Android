package com.sampoom.android.feature.auth.domain.repository

import com.sampoom.android.feature.auth.domain.model.User
import com.sampoom.android.feature.auth.domain.model.VendorList

interface AuthRepository {
    suspend fun signUp(
        email: String,
        password: String,
        workspace: String,
        branch: String,
        userName: String,
        position: String
    ): Result<User>

    suspend fun signIn(email: String, password: String): Result<User>
    suspend fun signOut(): Result<Unit>
    suspend fun refreshToken(): Result<User>
    suspend fun clearTokens(): Result<Unit>
    suspend fun isSignedIn(): Boolean
    suspend fun getProfile(workspace: String): Result<User>
    suspend fun getVendorList(): Result<VendorList>
}