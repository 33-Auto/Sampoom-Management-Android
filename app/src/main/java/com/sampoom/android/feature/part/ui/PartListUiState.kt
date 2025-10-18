package com.sampoom.android.feature.part.ui

import com.sampoom.android.feature.part.domain.model.Part

data class PartListUiState(
    val partList: List<Part> = emptyList(),
    val partListLoading: Boolean = false,
    val partListError: String? = null
)
