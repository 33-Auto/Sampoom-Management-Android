package com.sampoom.android.feature.outbound.domain.usecase

import com.sampoom.android.feature.outbound.domain.repository.OutboundRepository
import javax.inject.Inject

class UpdateOutboundQuantityUseCase @Inject constructor(
    private val repository: OutboundRepository
) {
    suspend operator fun invoke(outboundId: Long, quantity: Long): Result<Unit> = repository.updateOutboundQuantity(outboundId, quantity)
}