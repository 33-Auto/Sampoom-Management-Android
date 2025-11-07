package com.sampoom.android.feature.order.domain.model

data class OrderList(
    val items: List<Order>,
    val totalCount: Int = items.size,
    val isEmpty: Boolean = items.isEmpty()
) {
    companion object Companion {
        fun empty() = OrderList(emptyList())
    }
}
