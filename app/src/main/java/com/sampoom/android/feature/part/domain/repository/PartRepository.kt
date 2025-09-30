package com.sampoom.android.feature.part.domain.repository

import com.sampoom.android.feature.part.domain.model.PartList

interface PartRepository {
    suspend fun getPartList(): PartList
}