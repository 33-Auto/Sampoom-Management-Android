package com.sampoom.android.feature.part.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sampoom.android.feature.part.data.mapper.toModel
import com.sampoom.android.feature.part.data.remote.api.PartApi
import com.sampoom.android.feature.part.domain.model.SearchResult
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class PartPagingSource @AssistedInject constructor(
    private val api: PartApi,
    @Assisted private val keyword: String
) : PagingSource<Int, SearchResult>() {

    @AssistedFactory
    interface Factory {
        fun create(keyword: String): PartPagingSource
    }

    override fun getRefreshKey(state: PagingState<Int, SearchResult>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchResult> {
        return try {
            val page = params.key ?: 0
            val response = api.searchParts(keyword, page, 20)

            val flatParts = response.data.toModel()

            LoadResult.Page(
                data = flatParts,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (page < response.data.totalPages - 1) page + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}