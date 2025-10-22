package com.sampoom.android.feature.part.data.remote.api

import com.sampoom.android.core.network.ApiResponse
import com.sampoom.android.feature.part.data.remote.dto.CategoryDto
import com.sampoom.android.feature.part.data.remote.dto.GroupDto
import com.sampoom.android.feature.part.data.remote.dto.PartDto
import com.sampoom.android.feature.part.data.remote.dto.SearchDataDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// TODO: AgencyId 동적 주입
interface PartApi {
    @GET("agency/category")
    suspend fun getCategoryList(): ApiResponse<List<CategoryDto>>

    @GET("agency/category/{categoryId}")
    suspend fun getGroupList(@Path("categoryId") categoryId: Long): ApiResponse<List<GroupDto>>

    @GET("agency/1/group/{groupId}")
    suspend fun getPartList(@Path("groupId") groupId: Long): ApiResponse<List<PartDto>>

    @GET("agency/1/search")
    suspend fun searchParts(
        @Query("keyword") keyword: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10
    ): ApiResponse<SearchDataDto>
}