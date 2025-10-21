package com.sampoom.android.feature.order.data.repository

import com.sampoom.android.feature.order.data.mapper.toModel
import com.sampoom.android.feature.order.data.remote.api.OrderApi
import com.sampoom.android.feature.order.domain.model.OrderList
import com.sampoom.android.feature.order.domain.repository.OrderRepository
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val api: OrderApi
) : OrderRepository {
    override suspend fun getOrderList(): OrderList {
        val dto = api.getOrderList()
        val orderItems = dto.data.map { it.toModel() }
        return OrderList(items = orderItems)
    }

    override suspend fun createOrder(): OrderList {
        val dto = api.createOrder()
        val orderItems = dto.data.map { it.toModel() }
        return OrderList(items = orderItems)
    }

    override suspend fun receiveOrder(orderId: Long): Result<Unit> {
        return runCatching {
            val dto = api.receiveOrder(orderId)
            if (!dto.success) throw Exception(dto.message)
        }
    }

    override suspend fun getOrderDetail(orderId: Long): OrderList {
        val dto = api.getOrderDetail(orderId)
        val orderItems = dto.data.map { it.toModel() }
        return OrderList(items = orderItems)
    }

    override suspend fun cancelOrder(orderId: Long): Result<Unit> {
        return runCatching {
            val dto = api.cancelOrder(orderId)
            if (!dto.success) throw Exception(dto.message)
        }
    }
}