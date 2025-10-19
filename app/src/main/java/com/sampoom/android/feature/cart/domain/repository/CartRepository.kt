package com.sampoom.android.feature.cart.domain.repository

import com.sampoom.android.feature.cart.domain.model.CartList

interface CartRepository {
    suspend fun getCartList(): CartList
    suspend fun addCart(partId: Long, quantity: Long): Result<Unit>
    suspend fun deleteCart(cartItemId: Long): Result<Unit>
    suspend fun deleteAllCart(): Result<Unit>
    suspend fun updateCartQuantity(cartItemId: Long, quantity: Long): Result<Unit>
}