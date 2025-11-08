package com.sampoom.android.feature.user.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sampoom.android.core.preferences.AuthPreferences
import com.sampoom.android.feature.user.data.mapper.toModel
import com.sampoom.android.feature.user.data.remote.api.UserApi
import com.sampoom.android.feature.user.domain.model.Employee
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class EmployeePagingSource @AssistedInject constructor(
    private val api: UserApi,
    private val authPreferences: AuthPreferences
) : PagingSource<Int, Employee>() {

    @AssistedFactory
    interface Factory {
        fun create(): EmployeePagingSource
    }

    override fun getRefreshKey(state: PagingState<Int, Employee>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Employee> {
        return try {
            val agencyId = authPreferences.getStoredUser()?.agencyId ?: throw Exception()
            val page = params.key ?: 0
            val pageSize = params.loadSize
            val response = api.getEmployeeList("AGENCY", agencyId, page, pageSize)
            val employee = response.data.users.map { it.toModel() }
            val meta = response.data.meta

            LoadResult.Page(
                data = employee,
                prevKey = if (meta.hasPrevious) page - 1 else null,
                nextKey = if (meta.hasNext) page + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}