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

// TODO: AgencyId 동적 주입
interface CartApi {
    // 장바구니 목록 조회
    @GET("agency/1/cart")
    suspend fun getCartList(): ApiResponse<List<CartDto>>

    // 장바구니에 부품 추가
    @POST("agency/1/cart")
    suspend fun addCart(@Body body: AddCartRequestDto): ApiSuccessResponse

    // 장바구니 항목 삭제
    @DELETE("agency/1/cart/{cartItemId}")
    suspend fun deleteCart(@Path("cartItemId") cartItemId: Long): ApiSuccessResponse

    // 장바구니 수량 변경
    @PUT("agency/1/cart/{cartItemId}")
    suspend fun updateCart(
        @Path("cartItemId") cartItemId: Long,
        @Body body: UpdateCartRequestDto
    ): ApiSuccessResponse

    // 장바구니 전체 비우기
    @DELETE("agency/1/cart/clear")
    suspend fun deleteAllCart(): ApiSuccessResponse
}