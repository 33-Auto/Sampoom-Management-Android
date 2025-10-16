package com.sampoom.android.feature.part.domain.model

data class PartList(
    val items: List<Part>,
    val totalCount: Int = items.size,
    val isEmpty: Boolean = items.isEmpty()
) {
    companion object Companion {
        fun empty() = PartList(emptyList())
    }
}
