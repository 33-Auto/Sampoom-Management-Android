package com.sampoom.android.feature.part.domain.usecase

import com.sampoom.android.feature.part.domain.model.CategoryList
import com.sampoom.android.feature.part.domain.repository.PartRepository
import javax.inject.Inject

class GetCategoryUseCase @Inject constructor(
    private val repository: PartRepository
) {
    suspend operator fun invoke(): CategoryList = repository.getCategoryList()
}