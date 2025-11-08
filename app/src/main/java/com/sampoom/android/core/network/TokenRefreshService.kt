package com.sampoom.android.core.network

import com.sampoom.android.core.preferences.AuthPreferences
import com.sampoom.android.feature.auth.data.remote.api.AuthApi
import com.sampoom.android.feature.auth.data.remote.dto.RefreshRequestDto
import com.sampoom.android.feature.user.domain.model.User
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenRefreshService @Inject constructor(
    private val authPreferences: AuthPreferences
) {
    suspend fun refreshToken(): Result<User> = runCatching {
        val refreshToken = authPreferences.getRefreshToken()
            ?: throw Exception("No refresh token available")

        // 새로운 Retrofit 인스턴스 생성 (인터셉터 없이)
        val retrofit = Retrofit.Builder()
            .baseUrl("https://sampoom.store/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val authApi = retrofit.create(AuthApi::class.java)
        val response = authApi.refresh(RefreshRequestDto(refreshToken))

        val existingUser = authPreferences.getStoredUser()
            ?: throw Exception("No user information available")

        val updatedUser = existingUser.copy(
            accessToken = response.data.accessToken,
            refreshToken = response.data.refreshToken,
            expiresIn = response.data.expiresIn
        )

        authPreferences.saveUser(updatedUser)
        updatedUser
    }
}