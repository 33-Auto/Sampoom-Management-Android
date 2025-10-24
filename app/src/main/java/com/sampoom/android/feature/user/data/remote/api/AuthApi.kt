package com.sampoom.android.feature.user.data.remote.api

import com.sampoom.android.core.network.ApiResponse
import com.sampoom.android.core.network.ApiSuccessResponse
import com.sampoom.android.feature.user.data.remote.dto.LoginRequestDto
import com.sampoom.android.feature.user.data.remote.dto.SignUpRequestDto
import com.sampoom.android.feature.user.data.remote.dto.SignUpResponseDto
import com.sampoom.android.feature.user.data.remote.dto.LoginResponseDto
import com.sampoom.android.feature.user.data.remote.dto.RefreshRequestDto
import com.sampoom.android.feature.user.data.remote.dto.RefreshResponseDto
import com.sampoom.android.feature.user.data.remote.dto.UpdateRequestDto
import com.sampoom.android.feature.user.data.remote.dto.UpdateResponseDto
import com.sampoom.android.feature.user.data.remote.dto.VerifyRequestDto
import com.sampoom.android.feature.user.data.remote.dto.VerifyResponseDto
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/login")
    suspend fun login(@Body body: LoginRequestDto): ApiResponse<LoginResponseDto>

    @POST("auth/logout")
    suspend fun logout(): ApiSuccessResponse

    @POST("auth/refresh")
    suspend fun refresh(@Body body: RefreshRequestDto): ApiResponse<RefreshResponseDto>

    @POST("user/signup")
    suspend fun signUp(@Body body: SignUpRequestDto): ApiResponse<SignUpResponseDto>

    @POST("user/verify")
    suspend fun verify(@Body body: VerifyRequestDto): ApiResponse<VerifyResponseDto>

    @PATCH("user/update")
    suspend fun update(@Body body: UpdateRequestDto): ApiResponse<UpdateResponseDto>
}