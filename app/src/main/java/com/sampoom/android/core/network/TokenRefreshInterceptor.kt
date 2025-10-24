package com.sampoom.android.core.network

import com.sampoom.android.core.datastore.AuthPreferences
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class TokenRefreshInterceptor @Inject constructor(
    private val authPreferences: AuthPreferences,
    private val tokenRefreshService: TokenRefreshService
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        // 401 error
        if (response.code == 401) {
            try {
                val newUser = runBlocking {
                    tokenRefreshService.refreshToken().getOrThrow()
                }

                val newRequest = request.newBuilder()
                    .removeHeader("Authorization")
                    .addHeader("Authorization", "Bearer ${newUser.accessToken}")
                    .build()

                return chain.proceed(newRequest)
            } catch (e: Exception) {
                runBlocking {
                    authPreferences.clear()
                }
                return response
            }
        }

        return response
    }
}