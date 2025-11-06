package com.sampoom.android.feature.order.domain.usecase

import com.sampoom.android.feature.order.domain.repository.OrderRepository
import javax.inject.Inject

class ReceiveOrderUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(items: List<Pair<Long, Long>>): Result<Unit> = repository.receiveOrder(items)
}