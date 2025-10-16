package com.sampoom.android.feature.auth.domain.repository

import com.sampoom.android.feature.auth.domain.model.User

interface AuthRepository {
    suspend fun signUp(
        name: String,
        workspace: String,
        branch: String,
        position: String,
        email: String,
        password: String
    ): User

    suspend fun signIn(email: String, password: String): User
    suspend fun signOut()
    fun isSignedIn(): Boolean
}