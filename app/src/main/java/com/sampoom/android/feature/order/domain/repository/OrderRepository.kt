package com.sampoom.android.feature.order.domain.repository

import com.sampoom.android.feature.order.domain.model.OrderList

interface OrderRepository {
    suspend fun getOrderList(): OrderList
    suspend fun createOrder(): OrderList
    suspend fun receiveOrder(orderId: Long): Result<Unit>
    suspend fun getOrderDetail(orderId: Long): OrderList
    suspend fun cancelOrder(orderId: Long): Result<Unit>
}