package com.sampoom.android.feature.order.data.remote.api

import com.sampoom.android.core.model.ApiResponse
import com.sampoom.android.core.model.ApiSuccessResponse
import com.sampoom.android.feature.order.data.remote.dto.OrderDto
import com.sampoom.android.feature.order.data.remote.dto.OrderListDto
import com.sampoom.android.feature.order.data.remote.dto.OrderRequestDto
import com.sampoom.android.feature.order.data.remote.dto.ReceiveStockRequestDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface OrderApi {
    // 주문 목록 조회
    @GET("order/requested")
    suspend fun getOrderList(
        @Query("from") agencyName: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): ApiResponse<OrderListDto>

    // 주문 생성
    @POST("order/")
    suspend fun createOrder(@Body orderRequestDto: OrderRequestDto): ApiResponse<OrderDto>

    // 주문 완료 처리
    @PATCH("order/complete/{orderId}")
    suspend fun completeOrder(@Path("orderId") orderId: Long): ApiSuccessResponse

    // 주문 입고 처리 (대리점)
    @PATCH("agency/{agencyId}/stock")
    suspend fun receiveOrder(
        @Path("agencyId") agencyId: Long,
        @Body body: ReceiveStockRequestDto
    ): ApiSuccessResponse

    // 주문 상세 조회
    @GET("order/{orderId}")
    suspend fun getOrderDetail(@Path("orderId") orderId: Long): ApiResponse<OrderDto>

    // 주문 취소
    @PATCH("order/cancel/{orderId}")
    suspend fun cancelOrder(@Path("orderId") orderId: Long): ApiSuccessResponse
}