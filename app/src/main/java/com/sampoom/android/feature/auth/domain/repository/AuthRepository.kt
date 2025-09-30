package com.sampoom.android.feature.auth.domain.repository

import com.sampoom.android.feature.auth.domain.model.User

interface AuthRepository {
    suspend fun signIn(email: String, password: String): User
    suspend fun signOut()
    fun isSignedIn(): Boolean
}