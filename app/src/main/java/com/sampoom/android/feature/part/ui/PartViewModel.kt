package com.sampoom.android.feature.part.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sampoom.android.core.network.serverMessageOrNull
import com.sampoom.android.feature.part.domain.model.Category
import com.sampoom.android.feature.part.domain.usecase.GetCategoryUseCase
import com.sampoom.android.feature.part.domain.usecase.GetGroupUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PartViewModel @Inject constructor(
    private val getCategoryUseCase: GetCategoryUseCase,
    private val getGroupUseCase: GetGroupUseCase
) : ViewModel() {

    private companion object {
        private const val TAG = "PartViewModel"
    }

    private val _uiState = MutableStateFlow(PartUiState())
    val uiState: StateFlow<PartUiState> = _uiState

    private var groupLoadJob: Job? = null
    private var errorLabel: String = ""

    fun bindLabel(error: String) {
        errorLabel = error
    }

    init {
        loadCategory()
    }

    fun onEvent(event: PartUiEvent) {
        when (event) {
            is PartUiEvent.LoadCategories -> loadCategory()
            is PartUiEvent.CategorySelected -> selectCategory(event.category)
            is PartUiEvent.RetryCategories -> loadCategory()
            is PartUiEvent.RetryGroups -> loadGroup()
        }
    }

    private fun loadCategory() {
        viewModelScope.launch {
            _uiState.update { it.copy(categoryLoading = true, categoryError = null) }

            runCatching { getCategoryUseCase() }
                .onSuccess { categoryList ->
                    _uiState.update {
                        it.copy(
                            categoryList = categoryList.items,
                            categoryLoading = false,
                            categoryError = null
                        )
                    }
                }
                .onFailure { throwable ->
                    val backendMessage = throwable.serverMessageOrNull()
                    _uiState.update {
                        it.copy(
                            categoryLoading = false,
                            categoryError = backendMessage ?: (throwable.message ?: errorLabel)
                        )
                    }
                }
            Log.d(TAG, "loadCategory: ${_uiState.value}")
        }
    }

    private fun selectCategory(category: Category) {
        viewModelScope.launch {
            _uiState.update { it.copy(selectedCategory = category) }
            groupLoadJob?.cancel()  // 기존 그룹 로드 취소 후 새 요청
            loadGroup(category.id)
        }
    }

    private fun loadGroup(categoryId: Long) {
        groupLoadJob?.cancel()
        groupLoadJob = viewModelScope.launch {
            _uiState.update { it.copy(groupLoading = true, groupError = null) }

            runCatching { getGroupUseCase(categoryId) }
                .onSuccess { groupList ->
                    // 최신 선택과 불일치하면 무시
                    if (_uiState.value.selectedCategory?.id != categoryId) return@onSuccess
                    _uiState.update {
                        it.copy(
                            groupList = groupList.items,
                            groupLoading = false,
                            groupError = null
                        )
                    }
                }
                .onFailure { throwable ->
                    val backendMessage = throwable.serverMessageOrNull()
                    if (_uiState.value.selectedCategory?.id != categoryId) return@onFailure
                    _uiState.update {
                        it.copy(
                            groupLoading = false,
                            groupError = backendMessage ?: (throwable.message ?: errorLabel)
                        )
                    }
                }
            Log.d(TAG, "loadGroup: ${_uiState.value}")
        }
    }

    private fun loadGroup() {
        val selectedCategory = _uiState.value.selectedCategory
        if (selectedCategory != null) {
            loadGroup(selectedCategory.id)
        }
    }
}