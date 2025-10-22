package com.sampoom.android.feature.outbound.data.remote.api

import com.sampoom.android.core.network.ApiResponse
import com.sampoom.android.core.network.ApiSuccessResponse
import com.sampoom.android.feature.outbound.data.remote.dto.AddOutboundRequestDto
import com.sampoom.android.feature.outbound.data.remote.dto.OutboundDto
import com.sampoom.android.feature.outbound.data.remote.dto.UpdateOutboundRequestDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

// TODO: AgencyId 동적 주입
interface OutboundApi {
    // 출고 목록 조회
    @GET("agency/1/outbound")
    suspend fun getOutboundList(): ApiResponse<List<OutboundDto>>

    // 출고 목록에 부품 추가
    @POST("agency/1/outbound")
    suspend fun addOutbound(@Body body: AddOutboundRequestDto): ApiSuccessResponse

    // 출고 처리
    @POST("agency/1/outbound/process")
    suspend fun processOutbound(): ApiSuccessResponse

    // 출고 항목 삭제
    @DELETE("agency/1/outbound/{outboundId}")
    suspend fun deleteOutbound(@Path("outboundId") outboundId: Long): ApiSuccessResponse

    // 출고 수량 변경
    @PATCH("agency/1/outbound/{outboundId}")
    suspend fun updateOutbound(
        @Path("outboundId") outboundId: Long,
        @Body body: UpdateOutboundRequestDto
    ): ApiSuccessResponse

    // 출고 목록 전체 비우기
    @DELETE("agency/1/outbound/clear")
    suspend fun deleteAllOutbound(): ApiSuccessResponse
}