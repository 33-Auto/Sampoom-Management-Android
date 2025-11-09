package com.sampoom.android.feature.auth.data.repository

import com.sampoom.android.core.preferences.AuthPreferences
import com.sampoom.android.core.util.retry
import com.sampoom.android.feature.auth.data.mapper.toModel
import com.sampoom.android.feature.auth.data.remote.api.AuthApi
import com.sampoom.android.feature.auth.data.remote.dto.LoginRequestDto
import com.sampoom.android.feature.auth.data.remote.dto.RefreshRequestDto
import com.sampoom.android.feature.auth.data.remote.dto.SignUpRequestDto
import com.sampoom.android.feature.auth.domain.model.VendorList
import com.sampoom.android.feature.auth.domain.repository.AuthRepository
import com.sampoom.android.feature.user.domain.model.User
import kotlinx.coroutines.delay
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
    ): Result<User> {
        return runCatching {
            val signUpRes = api.signUp(
                SignUpRequestDto(
                    email = email,
                    password = password,
                    workspace = workspace,
                    branch = branch,
                    userName = userName,
                    position = position
                )
            )
            if (!signUpRes.success) throw Exception(signUpRes.message)

            delay(500)
            retry(times = 5, initialDelay = 500) {
                signIn(email, password).getOrThrow()
            }
        }
    }

    override suspend fun signIn(
        email: String,
        password: String
    ): Result<User> {
        return runCatching {
            val loginDto = api.login(
                LoginRequestDto(
                    workspace = "AGENCY",
                    email = email,
                    password = password
                )
            )
            if (!loginDto.success) throw Exception(loginDto.message)
            val loginUser = loginDto.data.toModel()

            preferences.saveUser(loginUser)
            loginUser
        }
    }

    override suspend fun signOut(): Result<Unit> {
        return runCatching {
            val dto = api.logout()
            if (!dto.success) throw Exception(dto.message)
        }.onSuccess {
            preferences.clear()
        }.onFailure {
            preferences.clear()
        }
    }

    override suspend fun refreshToken(): Result<User> {
        return runCatching {
            val refreshToken =
                preferences.getRefreshToken() ?: throw Exception("No refresh token available")
            val response = api.refresh(RefreshRequestDto(refreshToken))
            val existingUser =
                preferences.getStoredUser() ?: throw Exception("No user information available")

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
        }
    }

    override suspend fun isSignedIn(): Boolean = preferences.hasToken()

    override suspend fun getVendorList(): Result<VendorList> {
        return runCatching {
            val dto = api.getVendors()
            if (!dto.success) throw Exception(dto.message)
            val vendorItems = dto.data.map { it.toModel() }
            VendorList(items = vendorItems)
        }
    }
}