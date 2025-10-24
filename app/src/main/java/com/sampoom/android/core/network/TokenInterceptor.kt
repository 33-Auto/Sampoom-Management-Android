package com.sampoom.android.core.network

import com.sampoom.android.core.datastore.AuthPreferences
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class TokenInterceptor @Inject constructor(
    private val authPreferences: AuthPreferences
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        if (originalRequest.header("Authorization") == null) {
            val accessToken = authPreferences.getAccessToken()
            if (!accessToken.isNullOrEmpty()) {
                val newRequest = originalRequest.newBuilder()
                    .addHeader("Authorization", "Bearer $accessToken")
                    .build()
                return chain.proceed(newRequest)
            }
        }

        return chain.proceed(originalRequest)
    }
}