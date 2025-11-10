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
    /** 장바구니 리스트 조회 */
    override suspend fun getCartList(): Result<CartList> {
        return runCatching {
            val agencyId = authPreferences.getStoredUser()?.agencyId ?: throw Exception()
            val dto = api.getCartList(agencyId)
            val cartItems = dto.data.map { it.toModel() }
            CartList(items = cartItems)
        }
    }

    /** 장바구니 추가 */
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

    /** 장바구니 삭제 */
    override suspend fun deleteCart(cartItemId: Long): Result<Unit> {
        return runCatching {
            val agencyId = authPreferences.getStoredUser()?.agencyId ?: throw Exception()
            val dto = api.deleteCart(agencyId = agencyId, cartItemId = cartItemId)
            if (!dto.success) throw Exception(dto.message)
        }
    }

    /** 장바구니 전체 삭제 */
    override suspend fun deleteAllCart(): Result<Unit> {
        return runCatching {
            val agencyId = authPreferences.getStoredUser()?.agencyId ?: throw Exception()
            val dto = api.deleteAllCart(agencyId)
            if (!dto.success) throw Exception(dto.message)
        }
    }

    /** 장바구니 수량 수정 */
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