package com.sampoom.android.feature.cart.data.repository

import com.sampoom.android.core.preferences.AuthPreferences
import com.sampoom.android.feature.cart.data.mapper.toModel
import com.sampoom.android.feature.cart.data.remote.api.CartApi
import com.sampoom.android.feature.cart.data.remote.dto.AddCartRequestDto
import com.sampoom.android.feature.cart.data.remote.dto.UpdateCartRequestDto
import com.sampoom.android.feature.cart.domain.model.CartList
import com.sampoom.android.feature.cart.domain.repository.CartRepository
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val api: CartApi,
    private val authPreferences: AuthPreferences
) : CartRepository {
    override suspend fun getCartList(): Result<CartList> {
        return runCatching {
            val agencyId = authPreferences.getStoredUser()?.agencyId ?: throw Exception()
            val dto = api.getCartList(agencyId)
            val cartItems = dto.data.map { it.toModel() }
            CartList(items = cartItems)
        }
    }

    override suspend fun addCart(
        partId: Long,
        quantity: Long
    ): Result<Unit> {
        return runCatching {
            val agencyId = authPreferences.getStoredUser()?.agencyId ?: throw Exception()
            val dto = api.addCart(agencyId = agencyId, body = AddCartRequestDto(partId, quantity))
            if (!dto.success) throw Exception(dto.message)
        }
    }

    override suspend fun deleteCart(cartItemId: Long): Result<Unit> {
        return runCatching {
            val agencyId = authPreferences.getStoredUser()?.agencyId ?: throw Exception()
            val dto = api.deleteCart(agencyId = agencyId, cartItemId = cartItemId)
            if (!dto.success) throw Exception(dto.message)
        }
    }

    override suspend fun deleteAllCart(): Result<Unit> {
        return runCatching {
            val agencyId = authPreferences.getStoredUser()?.agencyId ?: throw Exception()
            val dto = api.deleteAllCart(agencyId)
            if (!dto.success) throw Exception(dto.message)
        }
    }

    override suspend fun updateCartQuantity(
        cartItemId: Long,
        quantity: Long
    ): Result<Unit> {
        return runCatching {
            val agencyId = authPreferences.getStoredUser()?.agencyId ?: throw Exception()
            val dto = api.updateCart(agencyId = agencyId, cartItemId = cartItemId, body = UpdateCartRequestDto(quantity))
            if (!dto.success) throw Exception(dto.message)
        }
    }
}