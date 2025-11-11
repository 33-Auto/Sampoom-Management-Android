package com.sampoom.android.feature.user.data.remote.api

import com.sampoom.android.core.model.ApiResponse
import com.sampoom.android.feature.user.data.remote.dto.EditEmployeeRequestDto
import com.sampoom.android.feature.user.data.remote.dto.EditEmployeeResponseDto
import com.sampoom.android.feature.user.data.remote.dto.EmployeeListDto
import com.sampoom.android.feature.user.data.remote.dto.GetProfileResponseDto
import com.sampoom.android.feature.user.data.remote.dto.UpdateEmployeeStatusRequestDto
import com.sampoom.android.feature.user.data.remote.dto.UpdateEmployeeStatusResponseDto
import com.sampoom.android.feature.user.data.remote.dto.UpdateProfileRequestDto
import com.sampoom.android.feature.user.data.remote.dto.UpdateProfileResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApi {
    // 프로필 조회
    @GET("user/profile")
    suspend fun getProfile(@Query("role") role: String): ApiResponse<GetProfileResponseDto>

    // 프로필 수정
    @PATCH("user/profile")
    suspend fun updateProfile(@Body body: UpdateProfileRequestDto): ApiResponse<UpdateProfileResponseDto>

    // 직원 프로필 수정
    @PATCH("user/profile/{userId}")
    suspend fun editEmployee(
        @Path("userId") userId: Long,
        @Query("role") role: String,
        @Body body: EditEmployeeRequestDto
    ): ApiResponse<EditEmployeeResponseDto>

    // 직원 목록 조회
    @GET("user/info")
    suspend fun getEmployeeList(
        @Query("role") role: String,
        @Query("organizationId") organizationId: Long,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): ApiResponse<EmployeeListDto>

    // 직원 상태 수정
    @PATCH("user/status/{userId}")
    suspend fun updateEmployeeStatus(
        @Path("userId") userId: Long,
        @Query("role") role: String,
        @Body body: UpdateEmployeeStatusRequestDto
    ): ApiResponse<UpdateEmployeeStatusResponseDto>
}