package com.sampoom.android.feature.part.data.remote.dto

data class SearchDataDto(
    val content: List<SearchCategoryDto>,
    val totalElements: Int,
    val totalPages: Int,
    val currentPage: Int
)

data class SearchCategoryDto(
    val categoryId: Long,
    val categoryName: String,
    val groups: List<SearchGroupDto>
)

data class SearchGroupDto(
    val groupId: Long,
    val groupName: String,
    val parts: List<PartDto>
)