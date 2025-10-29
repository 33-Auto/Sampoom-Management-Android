package com.sampoom.android.feature.user.data.repository

import android.R.attr.password
import com.sampoom.android.core.datastore.AuthPreferences
import com.sampoom.android.feature.user.data.mapper.mergeWith
import com.sampoom.android.feature.user.data.mapper.toModel
import com.sampoom.android.feature.user.data.remote.api.AuthApi
import com.sampoom.android.feature.user.data.remote.dto.LoginRequestDto
import com.sampoom.android.feature.user.data.remote.dto.RefreshRequestDto
import com.sampoom.android.feature.user.data.remote.dto.SignUpRequestDto
import com.sampoom.android.feature.user.domain.model.User
import com.sampoom.android.feature.user.domain.repository.AuthRepository
import kotlinx.coroutines.CancellationException
import javax.inject.Inject
import kotlin.math.log

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
    ): Result<User> {
        return runCatching {
            api.signUp(SignUpRequestDto(
                email = email,
                password = password,
                workspace = workspace,
                branch = branch,
                userName = userName,
                position = position
            ))
            signIn(email, password).getOrThrow()
        }
    }

    override suspend fun signIn(
        email: String,
        password: String
    ): Result<User> {
        return runCatching {
            val loginDto = api.login(LoginRequestDto(email, password))
            val loginUser = loginDto.data.toModel()
            preferences.saveUser(loginUser)

            val profileDto = getProfile()
            val profileUser = profileDto.getOrThrow()

            val user = loginUser.mergeWith(profileUser)

            preferences.saveUser(user)
            user
        }
    }

    override suspend fun signOut() : Result<Unit> {
        return runCatching {
            val dto = api.logout()
            if (!dto.success) throw Exception(dto.message)
            Result.success(Unit)
        }
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

    override suspend fun clearTokens(): Result<Unit> {
        return runCatching {
            preferences.clear()
            Result.success(Unit)
        }
    }

    override suspend fun isSignedIn(): Boolean = preferences.hasToken()

    override suspend fun getProfile(): Result<User> {
        return runCatching {
            val dto = api.getProfile()
            dto.data.toModel()
        }
    }
}