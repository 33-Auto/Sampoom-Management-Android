package com.sampoom.android.feature.auth.data.repository

import com.sampoom.android.feature.auth.data.local.preferences.AuthPreferences
import com.sampoom.android.feature.auth.data.mapper.toModel
import com.sampoom.android.feature.auth.data.remote.api.AuthApi
import com.sampoom.android.feature.auth.data.remote.dto.LoginRequestDto
import com.sampoom.android.feature.auth.domain.model.User
import com.sampoom.android.feature.auth.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    private val preferences: AuthPreferences
) : AuthRepository {
    override suspend fun signIn(
        email: String,
        password: String
    ): User {
        val dto = api.login(LoginRequestDto(email, password))
        preferences.saveToken(dto.token)
        return dto.toModel()
    }

    override suspend fun signOut() {
        preferences.clear()
    }

    override fun isSignedIn(): Boolean = preferences.hasToken()

}