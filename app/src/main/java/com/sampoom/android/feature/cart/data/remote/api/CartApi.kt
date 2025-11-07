package com.sampoom.android.feature.cart.data.remote.api

import com.sampoom.android.core.model.ApiResponse
import com.sampoom.android.core.model.ApiSuccessResponse
import com.sampoom.android.feature.cart.data.remote.dto.AddCartRequestDto
import com.sampoom.android.feature.cart.data.remote.dto.CartDto
import com.sampoom.android.feature.cart.data.remote.dto.UpdateCartRequestDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CartApi {
    // 장바구니 목록 조회
    @GET("agency/{agencyId}/cart")
    suspend fun getCartList(@Path("agencyId") agencyId: Long): ApiResponse<List<CartDto>>

    // 장바구니에 부품 추가
    @POST("agency/{agencyId}/cart")
    suspend fun addCart(
        @Path("agencyId") agencyId: Long,
        @Body body: AddCartRequestDto
    ): ApiSuccessResponse

    // 장바구니 항목 삭제
    @DELETE("agency/{agencyId}/cart/{cartItemId}")
    suspend fun deleteCart(
        @Path("agencyId") agencyId: Long,
        @Path("cartItemId") cartItemId: Long
    ): ApiSuccessResponse

    // 장바구니 수량 변경
    @PUT("agency/{agencyId}/cart/{cartItemId}")
    suspend fun updateCart(
        @Path("agencyId") agencyId: Long,
        @Path("cartItemId") cartItemId: Long,
        @Body body: UpdateCartRequestDto
    ): ApiSuccessResponse

    // 장바구니 전체 비우기
    @DELETE("agency/{agencyId}/cart/clear")
    suspend fun deleteAllCart(@Path("agencyId") agencyId: Long): ApiSuccessResponse
}