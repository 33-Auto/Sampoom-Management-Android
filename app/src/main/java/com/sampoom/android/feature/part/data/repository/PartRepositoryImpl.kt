package com.sampoom.android.feature.part.data.repository

import com.sampoom.android.feature.part.data.mapper.toModel
import com.sampoom.android.feature.part.data.remote.api.PartApi
import com.sampoom.android.feature.part.domain.model.CategoryList
import com.sampoom.android.feature.part.domain.model.GroupList
import com.sampoom.android.feature.part.domain.model.PartList
import com.sampoom.android.feature.part.domain.repository.PartRepository
import javax.inject.Inject

class PartRepositoryImpl @Inject constructor(
    private val api: PartApi
) : PartRepository {
    override suspend fun getCategoryList(): CategoryList {
        val dto = api.getCategoryList()
        val categoryItems = dto.data.map { it.toModel() }
        return CategoryList(items = categoryItems)
    }

    override suspend fun getGroupList(categoryId: Long): GroupList {
        val response = api.getGroupList(categoryId)
        val groupItems = response.data.map { it.toModel() }
        return GroupList(items = groupItems)
    }

    override suspend fun getPartList(groupId: Long): PartList {
        val response = api.getPartList(groupId)
        val partItems = response.data.map { it.toModel() }
        return PartList(items = partItems)
    }
}