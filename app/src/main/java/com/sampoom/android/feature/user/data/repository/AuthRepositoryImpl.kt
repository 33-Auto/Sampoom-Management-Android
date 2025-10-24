package com.sampoom.android.feature.user.data.repository

import com.sampoom.android.core.datastore.AuthPreferences
import com.sampoom.android.feature.user.data.mapper.toModel
import com.sampoom.android.feature.user.data.remote.api.AuthApi
import com.sampoom.android.feature.user.data.remote.dto.LoginRequestDto
import com.sampoom.android.feature.user.data.remote.dto.RefreshRequestDto
import com.sampoom.android.feature.user.data.remote.dto.SignUpRequestDto
import com.sampoom.android.feature.user.domain.model.User
import com.sampoom.android.feature.user.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    private val preferences: AuthPreferences
) : AuthRepository {
    override suspend fun signUp(
        email: String,
        password: String,
        workspace: String,
        branch: String,
        userName: String,
        position: String
    ): User {
        api.signUp(SignUpRequestDto(
            email = email,
            password = password,
            workspace = workspace,
            branch = branch,
            userName = userName,
            position = position
        ))
        return signIn(email, password)
    }

    override suspend fun signIn(
        email: String,
        password: String
    ): User {
        val dto = api.login(LoginRequestDto(email, password))
        val user = dto.data.toModel()
        preferences.saveUser(user)
        return user
    }

    override suspend fun signOut() {
        api.logout()
        preferences.clear()
    }

    override suspend fun refreshToken(): Result<User> {
        return runCatching {
            val refreshToken = preferences.getRefreshToken() ?: throw Exception("No refresh token available")
            val response = api.refresh(RefreshRequestDto(refreshToken))
            val existingUser = preferences.getStoredUser() ?: throw Exception("No user information available")

            val updatedUser = existingUser.copy(
                accessToken = response.data.accessToken,
                refreshToken = response.data.refreshToken,
                expiresIn = response.data.expiresIn
            )
            preferences.saveUser(updatedUser)
            updatedUser
        }
    }

    override suspend fun clearTokens() {
        preferences.clear()
    }

    override fun isSignedIn(): Boolean = preferences.hasToken()
}