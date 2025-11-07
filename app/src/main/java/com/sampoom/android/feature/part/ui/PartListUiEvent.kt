package com.sampoom.android.feature.part.ui

import com.sampoom.android.feature.part.domain.model.Part

sealed interface PartListUiEvent {
    object LoadPartList : PartListUiEvent
    object RetryPartList : PartListUiEvent
    data class ShowBottomSheet(val part: Part) : PartListUiEvent
    object DismissBottomSheet : PartListUiEvent
}