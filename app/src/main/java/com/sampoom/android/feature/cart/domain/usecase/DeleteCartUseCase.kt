package com.sampoom.android.feature.cart.domain.usecase

import com.sampoom.android.feature.cart.domain.repository.CartRepository
import javax.inject.Inject

class DeleteCartUseCase @Inject constructor(
    private val repository: CartRepository
) {
    suspend operator fun invoke(cartItemId: Long): Result<Unit> = repository.deleteCart(cartItemId)
}