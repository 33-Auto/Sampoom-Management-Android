package com.sampoom.android.feature.auth.data.remote.api

import com.sampoom.android.core.network.ApiResponse
import com.sampoom.android.feature.auth.data.remote.dto.LoginRequestDto
import com.sampoom.android.feature.auth.data.remote.dto.SignUpRequestDto
import com.sampoom.android.feature.auth.data.remote.dto.SignUpResponseDto
import com.sampoom.android.feature.auth.data.remote.dto.LoginResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("login")
    suspend fun login(@Body body: LoginRequestDto): ApiResponse<LoginResponseDto>

    @POST("signup")
    suspend fun signUp(@Body body: SignUpRequestDto): ApiResponse<SignUpResponseDto>
}