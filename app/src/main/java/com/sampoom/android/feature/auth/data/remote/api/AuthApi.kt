package com.sampoom.android.feature.auth.data.remote.api

import com.sampoom.android.feature.auth.data.remote.dto.LoginRequestDto
import com.sampoom.android.feature.auth.data.remote.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/login")
    suspend fun login(@Body body: LoginRequestDto): UserDto
}