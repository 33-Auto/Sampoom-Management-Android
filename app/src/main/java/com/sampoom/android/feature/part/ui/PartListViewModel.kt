package com.sampoom.android.feature.part.ui

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sampoom.android.core.network.serverMessageOrNull
import com.sampoom.android.feature.part.domain.usecase.GetPartUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PartListViewModel @Inject constructor(
    private val getPartListUseCase: GetPartUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private companion object {
        private const val TAG = "PartListViewModel"
    }

    private val _uiState = MutableStateFlow(PartListUiState())
    val uiState: StateFlow<PartListUiState> = _uiState

    // Navigation 인자 로드
    private val agencyId: Long = savedStateHandle.get<Long>("agencyId") ?: 0L
    private val groupId: Long = savedStateHandle.get<Long>("groupId") ?: 0L

    private var errorLabel: String = ""

    fun bindLabel(error: String) {
        errorLabel = error
    }

    init {
        if (groupId > 0L) loadPartList(groupId)
        else _uiState.update { it.copy(partListError = errorLabel) }
    }

    fun onEvent(event: PartListUiEvent) {
        when (event) {
            is PartListUiEvent.LoadPartList -> loadPartList(groupId)
            is PartListUiEvent.RetryPartList -> loadPartList(groupId)
            is PartListUiEvent.ShowBottomSheet -> _uiState.update { it.copy(selectedPart = event.part) }
            is PartListUiEvent.DismissBottomSheet -> _uiState.update { it.copy(selectedPart = null) }
        }
    }

    private fun loadPartList(groupId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(partListLoading = true, partListError = null) }

            runCatching { getPartListUseCase(groupId) }
                .onSuccess { partList ->
                    _uiState.update {
                        it.copy(
                            partList = partList.items,
                            partListLoading = false,
                            partListError = null
                        )
                    }
                }
                .onFailure { throwable ->
                    val backendMessage = throwable.serverMessageOrNull()
                    _uiState.update {
                        it.copy(
                            partListLoading = false,
                            partListError = backendMessage ?: (throwable.message ?: errorLabel)
                        )
                    }
                }
            Log.d(TAG, "submit: ${_uiState.value}")
        }
    }
}