package com.sampoom.android.feature.part.domain.repository

import androidx.paging.PagingData
import com.sampoom.android.feature.part.domain.model.CategoryList
import com.sampoom.android.feature.part.domain.model.GroupList
import com.sampoom.android.feature.part.domain.model.PartList
import com.sampoom.android.feature.part.domain.model.SearchResult
import kotlinx.coroutines.flow.Flow

interface PartRepository {
    suspend fun getCategoryList(): CategoryList
    suspend fun getGroupList(categoryId: Long): GroupList
    suspend fun getPartList(groupId: Long): PartList
    fun searchParts(keyword: String): Flow<PagingData<SearchResult>>
}