package com.sampoom.android.feature.order.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.sampoom.android.core.preferences.AuthPreferences
import com.sampoom.android.feature.cart.domain.model.CartList
import com.sampoom.android.feature.order.data.mapper.toModel
import com.sampoom.android.feature.order.data.paging.OrderPagingSource
import com.sampoom.android.feature.order.data.remote.api.OrderApi
import com.sampoom.android.feature.order.data.remote.dto.OrderCategoryDto
import com.sampoom.android.feature.order.data.remote.dto.OrderGroupDto
import com.sampoom.android.feature.order.data.remote.dto.OrderPartDto
import com.sampoom.android.feature.order.data.remote.dto.OrderRequestDto
import com.sampoom.android.feature.order.domain.model.Order
import com.sampoom.android.feature.order.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val api: OrderApi,
    private val preferences: AuthPreferences,
    private val pagingSourceFactory: OrderPagingSource.Factory
) : OrderRepository {
    override fun getOrderList(): Flow<PagingData<Order>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { pagingSourceFactory.create() }
        ).flow
    }

    override suspend fun createOrder(cartList: CartList): Result<Order> {
        return runCatching {
            val agencyName = preferences.getStoredUser()?.branch ?: throw Exception()
            val items = cartList.items.map { cart ->
                OrderCategoryDto(
                    categoryId = cart.categoryId,
                    categoryName = cart.categoryName,
                    groups = cart.groups.map { group ->
                        OrderGroupDto(
                            groupId = group.groupId,
                            groupName = group.groupName,
                            parts = group.parts.map { part ->
                                OrderPartDto(
                                    partId = part.partId,
                                    code = part.code,
                                    name = part.name,
                                    quantity = part.quantity
                                )
                            }
                        )
                    }
                )
            }

            val request = OrderRequestDto(
                agencyName = agencyName,
                items = items
            )

            val dto = api.createOrder(request)
            if (!dto.success) throw Exception(dto.message)
            dto.data.toModel()
        }
    }

    override suspend fun receiveOrder(orderId: Long): Result<Unit> {
        return runCatching {
            val dto = api.receiveOrder(orderId)
            if (!dto.success) throw Exception(dto.message)
        }
    }

    override suspend fun getOrderDetail(orderId: Long): Result<Order> {
        return runCatching {
            val dto = api.getOrderDetail(orderId)
            if (!dto.success) throw Exception(dto.message)
            dto.data.toModel()
        }
    }

    override suspend fun cancelOrder(orderId: Long): Result<Unit> {
        return runCatching {
            val dto = api.cancelOrder(orderId)
            if (!dto.success) throw Exception(dto.message)
        }
    }
}