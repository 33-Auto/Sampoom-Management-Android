package com.sampoom.android.feature.part.domain.usecase

import androidx.paging.PagingData
import com.sampoom.android.feature.part.domain.model.SearchResult
import com.sampoom.android.feature.part.domain.repository.PartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchPartsUseCase @Inject constructor(
    private val repository: PartRepository
) {
    operator fun invoke(keyword: String): Flow<PagingData<SearchResult>> = repository.searchParts(keyword)

}