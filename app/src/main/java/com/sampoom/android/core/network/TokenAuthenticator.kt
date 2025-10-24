package com.sampoom.android.core.network

import com.sampoom.android.core.datastore.AuthPreferences
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenAuthenticator @Inject constructor(
    private val authPreferences: AuthPreferences,
    private val tokenRefreshService: TokenRefreshService
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        // 이미 재시도된 요청인지 확인
        if (response.request.header("X-Retry-Count") != null) {
            return null // 재시도 제한
        }

        return try {
            val newUser = runBlocking {
                tokenRefreshService.refreshToken().getOrThrow()
            }

            // 새로운 토큰으로 요청 재시도
            response.request.newBuilder()
                .removeHeader("Authorization")
                .addHeader("Authorization", "Bearer ${newUser.accessToken}")
                .addHeader("X-Retry-Count", "1") // 재시도 표시
                .build()
        } catch (e: Exception) {
            runBlocking {
                authPreferences.clear()
            }
            null
        }
    }
}