package com.sampoom.android.feature.part.domain.usecase

import com.sampoom.android.feature.part.domain.model.PartList
import com.sampoom.android.feature.part.domain.repository.PartRepository
import javax.inject.Inject

class GetPartUseCase @Inject constructor(
    private val repository: PartRepository
) {
    suspend operator fun invoke(): PartList = repository.getPartList()
}