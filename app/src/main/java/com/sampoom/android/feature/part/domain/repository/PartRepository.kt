package com.sampoom.android.feature.part.domain.repository

import com.sampoom.android.feature.part.domain.model.CategoryList
import com.sampoom.android.feature.part.domain.model.GroupList
import com.sampoom.android.feature.part.domain.model.PartList

interface PartRepository {
    suspend fun getCategoryList(): CategoryList
    suspend fun getGroupList(categoryId: Long): GroupList
    suspend fun getPartList(groupId: Long): PartList
}