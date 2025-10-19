package com.sampoom.android.feature.part.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class PartDetailViewModel @Inject constructor(
) : ViewModel() {
    private val _uiState = MutableStateFlow(PartDetailUiState())
    val uiState: StateFlow<PartDetailUiState> = _uiState

    fun onEvent(event: PartDetailUiEvent) {
        when (event) {
            is PartDetailUiEvent.Initialize -> {
                _uiState.update {
                    it.copy(
                        part = event.part,
                        quantity = 1,
                        updateError = null
                    )
                }
            }
            is PartDetailUiEvent.IncreaseQuantity -> {
                val currentQuantity = _uiState.value.quantity
                _uiState.update { it.copy(quantity = currentQuantity + 1) }
            }
            is PartDetailUiEvent.DecreaseQuantity -> {
                val currentQuantity = _uiState.value.quantity
                _uiState.update { it.copy(quantity = currentQuantity - 1) }
            }
            is PartDetailUiEvent.SetQuantity -> {
                if (event.quantity > 0) {
                    _uiState.update { it.copy(quantity = event.quantity) }
                }
            }
            is PartDetailUiEvent.UpdateQuantity -> {
                val part = _uiState.value.part
                val quantity = _uiState.value.quantity
                if (part != null) {
                    // TODO : updatePartQuantity(part.partId, quantity)
                }
            }
            is PartDetailUiEvent.ClearError -> _uiState.update { it.copy(updateError = null) }
            is PartDetailUiEvent.Dismiss -> {
                _uiState.update {
                    it.copy(
                        part = null,
                        quantity = 1,
                        updateError = null
                    )
                }
            }
        }
    }
}