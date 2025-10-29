package com.sampoom.android.feature.outbound.domain.usecase

import com.sampoom.android.feature.outbound.domain.repository.OutboundRepository
import javax.inject.Inject

class DeleteAllOutboundUseCase @Inject constructor(
    private val repository: OutboundRepository
){
    suspend operator fun invoke(): Result<Unit> = repository.deleteAllOutbound()
}