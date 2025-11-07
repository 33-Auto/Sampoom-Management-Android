package com.sampoom.android.feature.order.domain.usecase

import androidx.paging.PagingData
import com.sampoom.android.feature.order.domain.model.Order
import com.sampoom.android.feature.order.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetOrderUseCase @Inject constructor(
    private val repository: OrderRepository
){
    operator fun invoke(): Flow<PagingData<Order>> = repository.getOrderList()
}