package com.sampoom.android.feature.part.domain.model

data class CategoryList(
    val items: List<Category>,
    val totalCount: Int = items.size,
    val isEmpty: Boolean = items.isEmpty()
) {
    companion object Companion {
        fun empty() = CategoryList(emptyList())
    }
}