package com.sampoom.android.feature.order.domain.usecase

import com.sampoom.android.feature.order.domain.model.OrderList
import com.sampoom.android.feature.order.domain.repository.OrderRepository
import javax.inject.Inject

class GetOrderUseCase @Inject constructor(
    private val repository: OrderRepository
){
    suspend operator fun invoke(): Result<OrderList> = repository.getOrderList()
}