package com.sampoom.android.feature.order.domain.usecase

import com.sampoom.android.feature.order.domain.repository.OrderRepository
import javax.inject.Inject

class CompleteOrderUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(orderId: Long): Result<Unit> = repository.completeOrder(orderId)
}