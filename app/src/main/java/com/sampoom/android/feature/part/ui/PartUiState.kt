package com.sampoom.android.feature.part.ui

import com.sampoom.android.feature.part.domain.model.Part

data class PartUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false,
    val partList: List<Part> = emptyList()
)