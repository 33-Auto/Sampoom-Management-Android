package com.sampoom.android.core.network

import com.sampoom.android.core.datastore.AuthPreferences
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class TokenInterceptor @Inject constructor(
    private val authPreferences: AuthPreferences
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val existingAuth = originalRequest.header("Authorization")
        if (existingAuth.isNullOrBlank()) {
            val accessToken = runBlocking {
                authPreferences.getAccessToken()
            }
            if (!accessToken.isNullOrEmpty()) {
                val newRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer $accessToken")
                    .build()
                return chain.proceed(newRequest)
            }
        }

        return chain.proceed(originalRequest)
    }
}