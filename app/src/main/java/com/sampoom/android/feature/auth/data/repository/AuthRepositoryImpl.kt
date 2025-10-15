package com.sampoom.android.feature.auth.data.repository

import com.sampoom.android.feature.auth.data.local.preferences.AuthPreferences
import com.sampoom.android.feature.auth.data.mapper.toModel
import com.sampoom.android.feature.auth.data.remote.api.AuthApi
import com.sampoom.android.feature.auth.data.remote.dto.LoginRequestDto
import com.sampoom.android.feature.auth.data.remote.dto.SignUpRequestDto
import com.sampoom.android.feature.auth.domain.model.User
import com.sampoom.android.feature.auth.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    private val preferences: AuthPreferences
) : AuthRepository {
    override suspend fun signUp(
        name: String,
        workspace: String,
        branch: String,
        position: String,
        email: String,
        password: String
    ): User {
        api.signUp(SignUpRequestDto(
            name = name,
            workspace = workspace,
            branch = branch,
            position = position,
            email = email,
            password = password
        ))
        return signIn(email, password)
    }

    override suspend fun signIn(
        email: String,
        password: String
    ): User {
        val dto = api.login(LoginRequestDto(email, password))
        preferences.saveToken(dto.data.accessToken, dto.data.refreshToken)
        return dto.data.toModel()
    }

    override suspend fun signOut() {
        preferences.clear()
    }

    override fun isSignedIn(): Boolean = preferences.hasToken()
}