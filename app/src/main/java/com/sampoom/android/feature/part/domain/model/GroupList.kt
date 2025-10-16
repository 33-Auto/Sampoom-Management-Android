package com.sampoom.android.feature.part.domain.model

data class GroupList(
    val items: List<Group>,
    val totalCount: Int = items.size,
    val isEmpty: Boolean = items.isEmpty()
) {
    companion object Companion {
        fun empty() = GroupList(emptyList())
    }
}
