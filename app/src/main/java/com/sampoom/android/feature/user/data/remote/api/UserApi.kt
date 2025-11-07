package com.sampoom.android.feature.user.data.remote.api

import com.sampoom.android.core.model.ApiResponse
import com.sampoom.android.feature.user.data.remote.dto.GetProfileResponseDto
import com.sampoom.android.feature.user.data.remote.dto.EditEmployeeRequestDto
import com.sampoom.android.feature.user.data.remote.dto.EditEmployeeResponseDto
import com.sampoom.android.feature.user.data.remote.dto.EmployeeListDto
import com.sampoom.android.feature.user.data.remote.dto.UpdateProfileRequestDto
import com.sampoom.android.feature.user.data.remote.dto.UpdateProfileResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApi {
    @GET("user/profile")
    suspend fun getProfile(@Query("workspace") workspace: String): ApiResponse<GetProfileResponseDto>

    @PATCH("user/profile")
    suspend fun updateProfile(@Body body: UpdateProfileRequestDto): ApiResponse<UpdateProfileResponseDto>

    @PATCH("user/profile/{userId}")
    suspend fun editEmployee(
        @Path("userId") userId: Long,
        @Query("workspace") workspace: String,
        @Body body: EditEmployeeRequestDto
    ): ApiResponse<EditEmployeeResponseDto>

    @GET("user/info")
    suspend fun getEmployeeList(
        @Query("workspace") workspace: String,
        @Query("organizationId") organizationId: Long,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): ApiResponse<EmployeeListDto>
}