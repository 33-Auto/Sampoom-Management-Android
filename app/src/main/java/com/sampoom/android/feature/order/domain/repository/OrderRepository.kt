package com.sampoom.android.feature.order.domain.repository

import androidx.paging.PagingData
import com.sampoom.android.feature.cart.domain.model.CartList
import com.sampoom.android.feature.order.domain.model.Order
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
//    suspend fun getOrderList(): Result<OrderList>
    fun getOrderList(): Flow<PagingData<Order>>
    suspend fun createOrder(cartList: CartList): Result<Order>
    suspend fun completeOrder(orderId: Long): Result<Unit>
    suspend fun receiveOrder(items: List<Pair<Long, Long>>): Result<Unit>
    suspend fun getOrderDetail(orderId: Long): Result<Order>
    suspend fun cancelOrder(orderId: Long): Result<Unit>
}