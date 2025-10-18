package com.sampoom.android.feature.part.domain.usecase

import com.sampoom.android.feature.part.domain.model.GroupList
import com.sampoom.android.feature.part.domain.repository.PartRepository
import javax.inject.Inject

class GetGroupUseCase @Inject constructor(
    private val repository: PartRepository
) {
    suspend operator fun invoke(categoryId: Long): GroupList = repository.getGroupList(categoryId)
}