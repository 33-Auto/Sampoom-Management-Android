package com.sampoom.android.feature.part.domain.model

data class SearchResult(
    val content: List<SearchCategory>,
    val totalElements: Int,
    val totalPages: Int,
    val currentPage: Int
)

data class SearchCategory(
    val categoryId: Long,
    val categoryName: String,
    val groups: List<SearchGroup>
)

data class SearchGroup(
    val groupId: Long,
    val groupName: String,
    val parts: List<Part>
)