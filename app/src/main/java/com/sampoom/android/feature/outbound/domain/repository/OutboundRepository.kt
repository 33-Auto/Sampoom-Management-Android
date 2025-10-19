package com.sampoom.android.feature.outbound.domain.repository

import com.sampoom.android.feature.outbound.domain.model.OutboundList

interface OutboundRepository {
    suspend fun getOutboundList(): OutboundList
    suspend fun addOutbound(partId: Long, quantity: Long): Result<Unit>
    suspend fun deleteOutbound(outboundId: Long): Result<Unit>
    suspend fun updateOutboundQuantity(outboundId: Long, quantity: Long): Result<Unit>
}