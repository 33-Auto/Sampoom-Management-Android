package com.sampoom.android.feature.order.domain.repository

import com.sampoom.android.feature.cart.domain.model.CartList
import com.sampoom.android.feature.order.domain.model.OrderList

interface OrderRepository {
    suspend fun getOrderList(): Result<OrderList>
    suspend fun createOrder(cartList: CartList): Result<OrderList>
    suspend fun receiveOrder(orderId: Long): Result<Unit>
    suspend fun getOrderDetail(orderId: Long): Result<OrderList>
    suspend fun cancelOrder(orderId: Long): Result<Unit>
}