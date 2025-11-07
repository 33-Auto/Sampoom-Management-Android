package com.sampoom.android.feature.order.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sampoom.android.core.preferences.AuthPreferences
import com.sampoom.android.feature.order.data.mapper.toModel
import com.sampoom.android.feature.order.data.remote.api.OrderApi
import com.sampoom.android.feature.order.domain.model.Order
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class OrderPagingSource @AssistedInject constructor(
    private val api: OrderApi,
    private val authPreferences: AuthPreferences
) : PagingSource<Int, Order>() {

    @AssistedFactory
    interface Factory {
        fun create(): OrderPagingSource
    }

    override fun getRefreshKey(state: PagingState<Int, Order>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Order> {
        return try {
            val agencyName = authPreferences.getStoredUser()?.branch ?: throw Exception()
            val page = params.key ?: 0
            val pageSize = params.loadSize
            val response = api.getOrderList(agencyName, page, pageSize)

            val orders = response.data.content.map { it.toModel() }

            LoadResult.Page(
                data = orders,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (page < response.data.totalPages - 1) page + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}