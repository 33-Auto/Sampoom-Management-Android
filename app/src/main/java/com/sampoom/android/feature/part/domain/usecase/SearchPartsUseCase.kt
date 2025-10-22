package com.sampoom.android.feature.part.domain.usecase

import com.sampoom.android.feature.part.domain.model.SearchResult
import com.sampoom.android.feature.part.domain.repository.PartRepository
import javax.inject.Inject

class SearchPartsUseCase @Inject constructor(
    private val repository: PartRepository
) {
    suspend operator fun invoke(keyword: String, page: Int = 0, size: Int = 20): SearchResult {
        return repository.searchParts(keyword, page, size)
    }
}