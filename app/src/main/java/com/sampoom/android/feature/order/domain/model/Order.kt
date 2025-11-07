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
    val quantity: Long,
    val standardCost: Long
)

// 파일 하단에 추가
val OrderPart.subtotal: Long
    get() = standardCost * quantity

val Order.totalCost: Long
    get() = items.sumOf { category ->
        category.groups.sumOf { group ->
            group.parts.sumOf { it.subtotal }
        }
    }