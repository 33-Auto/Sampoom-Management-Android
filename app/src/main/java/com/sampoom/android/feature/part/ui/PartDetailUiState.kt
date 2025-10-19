package com.sampoom.android.feature.part.ui

import com.sampoom.android.feature.part.domain.model.Part

data class PartDetailUiState(
    val part: Part? = null,
    val quantity: Long = 1,
    val isUpdating: Boolean = false,
    val updateError: String? = null
)