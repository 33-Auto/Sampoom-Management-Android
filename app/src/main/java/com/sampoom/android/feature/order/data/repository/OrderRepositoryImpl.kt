package com.sampoom.android.feature.order.data.repository

import com.sampoom.android.feature.order.data.mapper.toModel
import com.sampoom.android.feature.order.data.remote.api.OrderApi
import com.sampoom.android.feature.order.domain.model.OrderList
import com.sampoom.android.feature.order.domain.repository.OrderRepository
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class OrderRepositoryImpl @Inject constructor(
    private val api: OrderApi
) : OrderRepository {
    override suspend fun getOrderList(): Result<OrderList> {
        return runCatching {
            val dto = api.getOrderList()
            val orderItems = dto.data.map { it.toModel() }
            OrderList(items = orderItems)
        }.onFailure { exception ->
            if (exception is CancellationException) throw exception
        }
    }

    override suspend fun createOrder(): Result<OrderList> {
        return runCatching {
            val dto = api.createOrder()
            val orderItems = dto.data.map { it.toModel() }
            OrderList(items = orderItems)
        }.onFailure { exception ->
            if (exception is CancellationException) throw exception
        }
    }

    override suspend fun receiveOrder(orderId: Long): Result<Unit> {
        return runCatching {
            val dto = api.receiveOrder(orderId)
            if (!dto.success) throw Exception(dto.message)
        }.onFailure { exception ->
            if (exception is CancellationException) throw exception
        }
    }

    override suspend fun getOrderDetail(orderId: Long): Result<OrderList> {
        return runCatching {
            val dto = api.getOrderDetail(orderId)
            val orderItems = dto.data.map { it.toModel() }
            OrderList(items = orderItems)
        }.onFailure { exception ->
            if (exception is CancellationException) throw exception
        }
    }

    override suspend fun cancelOrder(orderId: Long): Result<Unit> {
        return runCatching {
            val dto = api.cancelOrder(orderId)
            if (!dto.success) throw Exception(dto.message)
        }.onFailure { exception ->
            if (exception is CancellationException) throw exception
        }
    }
}