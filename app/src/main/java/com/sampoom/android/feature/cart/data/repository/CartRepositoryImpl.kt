package com.sampoom.android.feature.cart.data.repository

import com.sampoom.android.feature.cart.data.mapper.toModel
import com.sampoom.android.feature.cart.data.remote.api.CartApi
import com.sampoom.android.feature.cart.data.remote.dto.AddCartRequestDto
import com.sampoom.android.feature.cart.data.remote.dto.UpdateCartRequestDto
import com.sampoom.android.feature.cart.domain.model.CartList
import com.sampoom.android.feature.cart.domain.repository.CartRepository
import kotlinx.coroutines.CancellationException
import javax.inject.Inject
import kotlin.Result

class CartRepositoryImpl @Inject constructor(
    private val api: CartApi
) : CartRepository {
    override suspend fun getCartList(): CartList {
        val dto = api.getCartList()
        val cartItems = dto.data.map { it.toModel() }
        return CartList(items = cartItems)
    }

    override suspend fun addCart(
        partId: Long,
        quantity: Long
    ): Result<Unit> {
        return try {
            val dto = api.addCart(AddCartRequestDto(partId, quantity))
            if (!dto.success) throw Exception(dto.message)
            Result.success(Unit)
        } catch (ce: CancellationException) {
            throw ce
        } catch (t : Throwable) {
            Result.failure(t)
        }
    }

    override suspend fun deleteCart(cartItemId: Long): Result<Unit> {
        return try {
            val dto = api.deleteCart(cartItemId)
            if (!dto.success) throw Exception(dto.message)
            Result.success(Unit)
        } catch (ce: CancellationException) {
            throw ce
        } catch (t : Throwable) {
            Result.failure(t)
        }
    }

    override suspend fun deleteAllCart(): Result<Unit> {
        return try {
            val dto = api.deleteAllCart()
            if (!dto.success) throw Exception(dto.message)
            Result.success(Unit)
        } catch (ce: CancellationException) {
            throw ce
        } catch (t : Throwable) {
            Result.failure(t)
        }
    }

    override suspend fun updateCartQuantity(
        cartItemId: Long,
        quantity: Long
    ): Result<Unit> {
        return try {
            val dto = api.updateCart(cartItemId, UpdateCartRequestDto(quantity))
            if (!dto.success) throw Exception(dto.message)
            Result.success(Unit)
        } catch (ce: CancellationException) {
            throw ce
        } catch (t : Throwable) {
            Result.failure(t)
        }
    }
}