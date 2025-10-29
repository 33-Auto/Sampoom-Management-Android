package com.sampoom.android.feature.user.data.remote.api

import com.sampoom.android.core.network.ApiResponse
import com.sampoom.android.core.network.ApiSuccessResponse
import com.sampoom.android.feature.user.data.remote.dto.LoginRequestDto
import com.sampoom.android.feature.user.data.remote.dto.SignUpRequestDto
import com.sampoom.android.feature.user.data.remote.dto.SignUpResponseDto
import com.sampoom.android.feature.user.data.remote.dto.LoginResponseDto
import com.sampoom.android.feature.user.data.remote.dto.RefreshRequestDto
import com.sampoom.android.feature.user.data.remote.dto.RefreshResponseDto
import com.sampoom.android.feature.user.data.remote.dto.UpdateProfileRequestDto
import com.sampoom.android.feature.user.data.remote.dto.UpdateProfileResponseDto
import com.sampoom.android.feature.user.data.remote.dto.GetProfileResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PATCH
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

    @GET("user/profile")
    suspend fun getProfile(): ApiResponse<GetProfileResponseDto>

    @PATCH("user/profile")
    suspend fun updateProfile(@Body body: UpdateProfileRequestDto): ApiResponse<UpdateProfileResponseDto>
}