package com.sampoom.android.feature.part.ui

import com.sampoom.android.feature.part.domain.model.Category

sealed interface PartUiEvent {
    object LoadCategories : PartUiEvent
    data class CategorySelected(val category: Category) : PartUiEvent
    object RetryCategories : PartUiEvent
    object RetryGroups : PartUiEvent
}