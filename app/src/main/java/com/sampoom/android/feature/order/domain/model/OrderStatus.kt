package com.sampoom.android.feature.order.domain.model

enum class OrderStatus {
    PENDING, COMPLETED, CANCELED;

    companion object {
        fun from(raw: String?): OrderStatus = when (raw?.uppercase()) {
            "PENDING" -> PENDING
            "COMPLETED" -> COMPLETED
            "CANCELED" -> CANCELED
            else -> PENDING
        }
    }
}