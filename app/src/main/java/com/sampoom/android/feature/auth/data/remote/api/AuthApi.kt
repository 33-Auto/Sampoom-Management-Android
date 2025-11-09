package com.sampoom.android.feature.auth.data.remote.api

import com.sampoom.android.core.model.ApiResponse
import com.sampoom.android.core.model.ApiSuccessResponse
import com.sampoom.android.feature.auth.data.remote.dto.GetVendorsResponseDto
import com.sampoom.android.feature.auth.data.remote.dto.LoginRequestDto
import com.sampoom.android.feature.auth.data.remote.dto.LoginResponseDto
import com.sampoom.android.feature.auth.data.remote.dto.RefreshRequestDto
import com.sampoom.android.feature.auth.data.remote.dto.RefreshResponseDto
import com.sampoom.android.feature.auth.data.remote.dto.SignUpRequestDto
import com.sampoom.android.feature.auth.data.remote.dto.SignUpResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/signup")
    @Headers("X-No-Auth: true")
    suspend fun signUp(@Body body: SignUpRequestDto): ApiResponse<SignUpResponseDto>

    @POST("auth/refresh")
    suspend fun refresh(@Body body: RefreshRequestDto): ApiResponse<RefreshResponseDto>

    @POST("auth/logout")
    suspend fun logout(): ApiSuccessResponse

    @POST("auth/login")
    @Headers("X-No-Auth: true")
    suspend fun login(@Body body: LoginRequestDto): ApiResponse<LoginResponseDto>

    @GET("site/vendors")
    suspend fun getVendors(): ApiResponse<List<GetVendorsResponseDto>>
}