package com.sampoom.android.feature.outbound.data.remote.api

import com.sampoom.android.core.model.ApiResponse
import com.sampoom.android.core.model.ApiSuccessResponse
import com.sampoom.android.feature.outbound.data.remote.dto.AddOutboundRequestDto
import com.sampoom.android.feature.outbound.data.remote.dto.OutboundDto
import com.sampoom.android.feature.outbound.data.remote.dto.UpdateOutboundRequestDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface OutboundApi {
    // 출고 목록 조회
    @GET("agency/{agencyId}/outbound")
    suspend fun getOutboundList(@Path("agencyId") agencyId: Long): ApiResponse<List<OutboundDto>>

    // 출고 목록에 부품 추가
    @POST("agency/{agencyId}/outbound")
    suspend fun addOutbound(
        @Path("agencyId") agencyId: Long,
        @Body body: AddOutboundRequestDto
    ): ApiSuccessResponse

    // 출고 처리
    @POST("agency/{agencyId}/outbound/process")
    suspend fun processOutbound(@Path("agencyId") agencyId: Long): ApiSuccessResponse

    // 출고 항목 삭제
    @DELETE("agency/{agencyId}/outbound/{outboundId}")
    suspend fun deleteOutbound(
        @Path("agencyId") agencyId: Long,
        @Path("outboundId") outboundId: Long
    ): ApiSuccessResponse

    // 출고 수량 변경
    @PATCH("agency/{agencyId}/outbound/{outboundId}")
    suspend fun updateOutbound(
        @Path("agencyId") agencyId: Long,
        @Path("outboundId") outboundId: Long,
        @Body body: UpdateOutboundRequestDto
    ): ApiSuccessResponse

    // 출고 목록 전체 비우기
    @DELETE("agency/{agencyId}/outbound/clear")
    suspend fun deleteAllOutbound(@Path("agencyId") agencyId: Long): ApiSuccessResponse
}