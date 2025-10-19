package com.sampoom.android.feature.cart.domain.usecase

import com.sampoom.android.feature.cart.domain.repository.CartRepository
import javax.inject.Inject

class AddCartUseCase @Inject constructor(
    private val repository: CartRepository
) {
    suspend operator fun invoke(partId: Long, quantity: Long) = repository.addCart(partId, quantity)
}