package com.sampoom.android.feature.order.data.remote.dto

import com.sampoom.android.feature.order.domain.model.OrderStatus

data class OrderDto(
    val orderId: Long,
    val orderNumber: String?,
    val createdAt: String?,
    val status: OrderStatus,
    val agencyName: String?,
    val items: List<OrderCategoryDto>
)

data class OrderCategoryDto(
    val categoryId: Long,
    val categoryName: String,
    val groups: List<OrderGroupDto>
)

data class OrderGroupDto(
    val groupId: Long,
    val groupName: String,
    val parts: List<OrderPartDto>
)

data class OrderPartDto(
    val partId: Long,
    val code: String,
    val name: String,
    val quantity: Long,
    val standardCost: Long
)