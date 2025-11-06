package com.sampoom.android.feature.dashboard.data.remote.api

import com.sampoom.android.core.model.ApiResponse
import com.sampoom.android.feature.dashboard.data.remote.dto.DashboardResponseDto
import com.sampoom.android.feature.dashboard.data.remote.dto.WeeklySummaryResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface DashboardApi {
    // 대시보드 조회
    @GET("agency/{agencyId}/dashboard")
    suspend fun getDashboard(@Path("agencyId") agencyId: Long): ApiResponse<DashboardResponseDto>

    // 주간 히스토리 조회
    @GET("agency/{agencyId}/weekly-summary")
    suspend fun getWeeklySummary(@Path("agencyId") agencyId: Long): ApiResponse<WeeklySummaryResponseDto>
}