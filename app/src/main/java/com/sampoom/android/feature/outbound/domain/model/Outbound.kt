package com.sampoom.android.feature.outbound.domain.model

data class Outbound(
    val categoryId: Long,
    val categoryName: String,
    val groups: List<OutboundGroup>
)

data class OutboundGroup(
    val groupId: Long,
    val groupName: String,
    val parts: List<OutboundPart>
)

data class OutboundPart(
    val outboundId: Long,
    val partId: Long,
    val code: String,
    val name: String,
    val quantity: Long,
    val standardCost: Long
)

// 품목 합계(단가 x 수량)
val OutboundPart.subtotal: Long
    get() = standardCost * quantity