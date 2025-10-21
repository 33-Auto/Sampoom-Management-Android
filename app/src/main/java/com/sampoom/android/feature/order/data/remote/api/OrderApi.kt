package com.sampoom.android.feature.order.data.remote.api

import com.sampoom.android.core.network.ApiResponse
import com.sampoom.android.core.network.ApiSuccessResponse
import com.sampoom.android.feature.order.data.remote.dto.OrderDto
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

// TODO: AgencyId 동적 주입
interface OrderApi {
    // 주문 목록 조회
    @GET("agency/1/orders")
    suspend fun getOrderList(): ApiResponse<List<OrderDto>>

    // 주문 생성
    @POST("agency/1/orders")
    suspend fun createOrder(): ApiResponse<List<OrderDto>>

    // 주문 입고 처리
    @PATCH("agency/1/orders/{orderId}/receive")
    suspend fun receiveOrder(@Path("orderId") orderId: Long): ApiSuccessResponse

    // 주문 상세 조회
    @GET("agency/1/orders/{orderId}")
    suspend fun getOrderDetail(@Path("orderId") orderId: Long): ApiResponse<List<OrderDto>>

    // 주문 취소
    @DELETE("agency/1/orders/{orderId}")
    suspend fun cancelOrder(@Path("orderId") orderId: Long): ApiSuccessResponse
}