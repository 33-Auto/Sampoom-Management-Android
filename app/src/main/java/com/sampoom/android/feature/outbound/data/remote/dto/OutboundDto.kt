package com.sampoom.android.feature.outbound.data.remote.dto

data class OutboundDto(
    val categoryId: Long,
    val categoryName: String,
    val groups: List<OutboundGroupDto>
)

data class OutboundGroupDto(
    val groupId: Long,
    val groupName: String,
    val parts: List<OutboundPartDto>
)

data class OutboundPartDto(
    val outboundId: Long,
    val partId: Long,
    val code: String,
    val name: String,
    val quantity: Long
)