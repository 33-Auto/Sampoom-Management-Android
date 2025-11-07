package com.sampoom.android.feature.outbound.domain.usecase

import com.sampoom.android.feature.outbound.domain.repository.OutboundRepository
import javax.inject.Inject

class DeleteOutboundUseCase @Inject constructor(
    private val repository: OutboundRepository
){
    suspend operator fun invoke(outboundId: Long): Result<Unit> = repository.deleteOutbound(outboundId)
}