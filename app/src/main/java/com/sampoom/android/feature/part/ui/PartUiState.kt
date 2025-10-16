package com.sampoom.android.feature.part.ui

import com.sampoom.android.feature.part.domain.model.Category
import com.sampoom.android.feature.part.domain.model.Group

data class PartUiState(
    // Part
    val groupList: List<Group> = emptyList(),
    val groupLoading: Boolean = false,
    val groupError: String? = null,

    // 선택된 Category
    val selectedCategory: Category? = null,

    // Category
    val categoryList: List<Category> = emptyList(),
    val categoryLoading: Boolean = false,
    val categoryError: String? = null
)