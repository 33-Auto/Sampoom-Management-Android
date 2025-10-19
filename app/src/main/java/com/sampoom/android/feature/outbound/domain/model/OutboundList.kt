package com.sampoom.android.feature.outbound.domain.model

data class OutboundList(
    val items: List<Outbound>,
    val totalCount: Int = items.size,
    val isEmpty: Boolean = items.isEmpty()
) {
    companion object Companion {
        fun empty() = OutboundList(emptyList())
    }
}
