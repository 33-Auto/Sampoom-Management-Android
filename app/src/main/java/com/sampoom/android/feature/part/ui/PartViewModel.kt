package com.sampoom.android.feature.part.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sampoom.android.core.network.serverMessageOrNull
import com.sampoom.android.feature.part.domain.model.Category
import com.sampoom.android.feature.part.domain.model.SearchResult
import com.sampoom.android.feature.part.domain.usecase.GetCategoryUseCase
import com.sampoom.android.feature.part.domain.usecase.GetGroupUseCase
import com.sampoom.android.feature.part.domain.usecase.SearchPartsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PartViewModel @Inject constructor(
    private val getCategoryUseCase: GetCategoryUseCase,
    private val getGroupUseCase: GetGroupUseCase,
    private val searchPartsUseCase: SearchPartsUseCase
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

    private val _searchKeyword = MutableStateFlow<String?>(null)

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val searchResult: Flow<PagingData<SearchResult>> = _searchKeyword
        .debounce(300)
        .distinctUntilChanged()
        .flatMapLatest { keyword ->
            if (keyword.isNullOrBlank()) {
                flowOf(PagingData.empty())
            } else {
                searchPartsUseCase(keyword)
            }
        }
        .cachedIn(viewModelScope)

    fun onEvent(event: PartUiEvent) {
        when (event) {
            is PartUiEvent.LoadCategories -> loadCategory()
            is PartUiEvent.CategorySelected -> selectCategory(event.category)
            is PartUiEvent.RetryCategories -> loadCategory()
            is PartUiEvent.RetryGroups -> loadGroup()
            is PartUiEvent.Search -> {
                _searchKeyword.value = event.keyword
                _uiState.update {
                    it.copy(keyword = event.keyword)
                }
            }
            is PartUiEvent.SetKeyword -> _uiState.update { it.copy(keyword = event.keyword) }
        }
    }

    private fun loadCategory() {
        viewModelScope.launch {
            _uiState.update { it.copy(categoryLoading = true, categoryError = null) }

            getCategoryUseCase()
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

            getGroupUseCase(categoryId)
                .onSuccess { groupList ->
                    if (_uiState.value.selectedCategory?.id != categoryId) return@launch
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
                    if (_uiState.value.selectedCategory?.id != categoryId) return@launch
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