package com.sampoom.android.feature.order.domain.model

import java.util.Locale

enum class OrderStatus {
    PENDING, CONFIRMED, SHIPPING, DELAYED, PRODUCING, ARRIVED, COMPLETED, CANCELED;

    companion object {
        fun from(raw: String?): OrderStatus = when (raw?.uppercase(Locale.ROOT)) {
            "PENDING" -> PENDING
            "CONFIRMED" -> CONFIRMED
            "SHIPPING" -> SHIPPING
            "DELAYED" -> DELAYED
            "PRODUCING" -> PRODUCING
            "ARRIVED" -> ARRIVED
            "COMPLETED" -> COMPLETED
            "CANCELED" -> CANCELED
            else -> PENDING
        }
    }
}