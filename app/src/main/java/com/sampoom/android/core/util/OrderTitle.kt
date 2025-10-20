package com.sampoom.android.core.util

import com.sampoom.android.feature.order.domain.model.Order
import com.sampoom.android.feature.order.domain.model.OrderPart

fun buildOrderTitle(order: Order): String {
    val flattened: List<Triple<String /*category*/, String /*group*/, OrderPart>> =
        order.items.flatMap { category ->
            category.groups.flatMap { group ->
                group.parts.map { part -> Triple(category.categoryName, group.groupName, part) }
            }
        }

    if (flattened.isEmpty()) return "-"

    val first = flattened.first()
    val groupName = first.second
    val part = first.third
    val totalParts = flattened.size

    return if (totalParts == 1) {
        "$groupName - ${part.name} ${part.quantity}EA"
    } else {
        "${part.name} - $groupName ${part.quantity}EA 외 ${totalParts - 1}건"
    }
}