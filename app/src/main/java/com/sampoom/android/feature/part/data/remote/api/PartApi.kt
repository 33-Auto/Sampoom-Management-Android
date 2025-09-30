package com.sampoom.android.feature.part.data.remote.api

import com.sampoom.android.core.network.ApiResponse
import com.sampoom.android.feature.part.data.remote.dto.PartDto
import retrofit2.http.GET

interface PartApi {
    @GET("part")
    suspend fun getPartList(): ApiResponse<List<PartDto>>
}