package com.sampoom.android.feature.part.data.repository

import com.sampoom.android.feature.part.data.mapper.toModel
import com.sampoom.android.feature.part.data.remote.api.PartApi
import com.sampoom.android.feature.part.domain.model.PartList
import com.sampoom.android.feature.part.domain.repository.PartRepository
import javax.inject.Inject

class PartRepositoryImpl @Inject constructor(
    private val api: PartApi
) : PartRepository {
    override suspend fun getPartList(): PartList {
        val response = api.getPartList()
        val partItems = response.data.map { it.toModel() }
        return PartList(items = partItems)
    }
}