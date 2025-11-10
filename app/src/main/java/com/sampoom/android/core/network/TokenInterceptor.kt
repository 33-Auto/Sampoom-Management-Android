package com.sampoom.android.core.network

import com.sampoom.android.core.preferences.AuthPreferences
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

/** 토큰 Interceptor */
class TokenInterceptor @Inject constructor(
    private val authPreferences: AuthPreferences,
    private val tokenLogoutEmitter: TokenLogoutEmitter
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // 스킵 플래그가 있으면 토큰 주입 없이 진행
        if (originalRequest.header("X-No-Auth") == "true") {
            val requestWithoutFlag = originalRequest.newBuilder()
                .removeHeader("X-No-Auth")
                .build()
            return proceedAndLogoutOnForbidden(chain, requestWithoutFlag)
        }

        val existingAuth = originalRequest.header("Authorization")
        if (existingAuth.isNullOrBlank()) {
            val accessToken = runBlocking {
                authPreferences.getAccessToken()
            }
            if (!accessToken.isNullOrEmpty()) {
                val newRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer $accessToken")
                    .build()
                return proceedAndLogoutOnForbidden(chain, newRequest)
            }
        }

        return proceedAndLogoutOnForbidden(chain, originalRequest)
    }

    private fun proceedAndLogoutOnForbidden(chain: Interceptor.Chain, request: Request): Response {
        val response = chain.proceed(request)
        if (response.code == 401) {
            runBlocking {
                authPreferences.clear()
                tokenLogoutEmitter.emit()
            }
        }
        return response
    }
}