package com.sampoom.android.feature.part.data.remote.api

import com.sampoom.android.core.model.ApiResponse
import com.sampoom.android.feature.part.data.remote.dto.CategoryDto
import com.sampoom.android.feature.part.data.remote.dto.GroupDto
import com.sampoom.android.feature.part.data.remote.dto.PartDto
import com.sampoom.android.feature.part.data.remote.dto.SearchDataDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PartApi {
    // 카테고리 조회
    @GET("agency/category")
    suspend fun getCategoryList(): ApiResponse<List<CategoryDto>>

    // 그룹 조회
    @GET("agency/category/{categoryId}")
    suspend fun getGroupList(@Path("categoryId") categoryId: Long): ApiResponse<List<GroupDto>>

    // 부품 리스트 조회
    @GET("agency/{agencyId}/group/{groupId}")
    suspend fun getPartList(
        @Path("agencyId") agencyId: Long,
        @Path("groupId") groupId: Long
    ): ApiResponse<List<PartDto>>

    // 부품 검색
    @GET("agency/{agencyId}/search")
    suspend fun searchParts(
        @Path("agencyId") agencyId: Long,
        @Query("keyword") keyword: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): ApiResponse<SearchDataDto>
}