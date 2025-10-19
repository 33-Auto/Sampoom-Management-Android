package com.sampoom.android.feature.cart.domain.usecase

import com.sampoom.android.feature.cart.domain.repository.CartRepository
import javax.inject.Inject

class DeleteAllCartUseCase @Inject constructor(
    private val repository: CartRepository
) {
    suspend operator fun invoke() = repository.deleteAllCart()
}