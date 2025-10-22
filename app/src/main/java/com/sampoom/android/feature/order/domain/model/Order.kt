package com.sampoom.android.feature.order.domain.model

data class Order(
    val orderId: Long,
    val orderNumber: String?,
    val createdAt: String?,
    val status: OrderStatus,
    val agencyName: String?,
    val items: List<OrderCategory>
)

data class OrderCategory(
    val categoryId: Long,
    val categoryName: String,
    val groups: List<OrderGroup>
)

data class OrderGroup(
    val groupId: Long,
    val groupName: String,
    val parts: List<OrderPart>
)

data class OrderPart(
    val partId: Long,
    val code: String,
    val name: String,
    val quantity: Long
)
