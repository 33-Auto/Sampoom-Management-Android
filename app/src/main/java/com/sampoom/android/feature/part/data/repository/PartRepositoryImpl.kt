package com.sampoom.android.feature.part.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.sampoom.android.feature.part.data.mapper.toModel
import com.sampoom.android.feature.part.data.paging.PartPagingSource
import com.sampoom.android.feature.part.data.remote.api.PartApi
import com.sampoom.android.feature.part.domain.model.CategoryList
import com.sampoom.android.feature.part.domain.model.GroupList
import com.sampoom.android.feature.part.domain.model.PartList
import com.sampoom.android.feature.part.domain.model.SearchResult
import com.sampoom.android.feature.part.domain.repository.PartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class PartRepositoryImpl @Inject constructor(
    private val api: PartApi,
    private val pagingSourceFactory: PartPagingSource.Factory
) : PartRepository {
    override suspend fun getCategoryList(): Result<CategoryList> {
        return runCatching {
            val dto = api.getCategoryList()
            val categoryItems = dto.data.map { it.toModel() }
            CategoryList(items = categoryItems)
        }.onFailure { exception ->
            if (exception is CancellationException) throw exception
        }
    }

    override suspend fun getGroupList(categoryId: Long): Result<GroupList> {
        return runCatching {
            val response = api.getGroupList(categoryId)
            val groupItems = response.data.map { it.toModel() }
            GroupList(items = groupItems)
        }.onFailure { exception ->
            if (exception is CancellationException) throw exception
        }
    }

    override suspend fun getPartList(groupId: Long): Result<PartList> {
        return runCatching {
            val response = api.getPartList(groupId)
            val partItems = response.data.map { it.toModel() }
            PartList(items = partItems)
        }.onFailure { exception ->
            if (exception is CancellationException) throw exception
        }
    }

    override fun searchParts(keyword: String): Flow<PagingData<SearchResult>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { pagingSourceFactory.create(keyword) }
        ).flow
    }
}