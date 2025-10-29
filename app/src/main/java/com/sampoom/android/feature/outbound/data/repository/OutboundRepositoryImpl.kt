package com.sampoom.android.feature.outbound.data.repository

import com.sampoom.android.feature.outbound.data.mapper.toModel
import com.sampoom.android.feature.outbound.data.remote.api.OutboundApi
import com.sampoom.android.feature.outbound.data.remote.dto.AddOutboundRequestDto
import com.sampoom.android.feature.outbound.data.remote.dto.UpdateOutboundRequestDto
import com.sampoom.android.feature.outbound.domain.model.OutboundList
import com.sampoom.android.feature.outbound.domain.repository.OutboundRepository
import jakarta.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class OutboundRepositoryImpl @Inject constructor(
    private val api: OutboundApi
) : OutboundRepository {
    override suspend fun getOutboundList(): Result<OutboundList> {
        return runCatching {
            val dto = api.getOutboundList()
            val outboundItems = dto.data.map { it.toModel() }
            OutboundList(items = outboundItems)
        }
    }

    override suspend fun processOutbound(): Result<Unit> {
        return try {
            val dto = api.processOutbound()
            if (!dto.success) throw Exception(dto.message)
            Result.success(Unit)
        } catch (ce: CancellationException) {
            throw ce
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

    override suspend fun addOutbound(
        partId: Long,
        quantity: Long
    ): Result<Unit> {
        return try {
            val dto = api.addOutbound(AddOutboundRequestDto(partId, quantity))
            if (!dto.success) throw Exception(dto.message)
            Result.success(Unit)
        } catch (ce: CancellationException) {
            throw ce
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

    override suspend fun deleteOutbound(outboundId: Long): Result<Unit> {
        return try {
            val dto = api.deleteOutbound(outboundId)
            if (!dto.success) throw Exception(dto.message)
            Result.success(Unit)
        } catch (ce: CancellationException) {
            throw ce
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

    override suspend fun deleteAllOutbound(): Result<Unit> {
        return try {
            val dto = api.deleteAllOutbound()
            if (!dto.success) throw Exception(dto.message)
            Result.success(Unit)
        } catch (ce: CancellationException) {
            throw ce
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

    override suspend fun updateOutboundQuantity(
        outboundId: Long,
        quantity: Long
    ): Result<Unit> {
        return try {
            val dto = api.updateOutbound(outboundId, UpdateOutboundRequestDto(quantity))
            if (!dto.success) throw Exception(dto.message)
            Result.success(Unit)
        } catch (ce: CancellationException) {
            throw ce
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }
}