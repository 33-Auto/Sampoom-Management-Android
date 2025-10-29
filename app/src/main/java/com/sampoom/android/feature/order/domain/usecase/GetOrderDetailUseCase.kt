package com.sampoom.android.feature.order.domain.usecase

import com.sampoom.android.feature.order.domain.model.OrderList
import com.sampoom.android.feature.order.domain.repository.OrderRepository
import javax.inject.Inject

class GetOrderDetailUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(orderId: Long): Result<OrderList> = repository.getOrderDetail(orderId)
}