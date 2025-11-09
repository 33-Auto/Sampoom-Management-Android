package com.sampoom.android.feature.user.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sampoom.android.core.util.GlobalMessageHandler
import com.sampoom.android.feature.user.domain.model.Employee
import com.sampoom.android.feature.user.domain.usecase.GetEmployeeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class EmployeeListViewModel @Inject constructor(
    private val messageHandler: GlobalMessageHandler,
    private val getEmployeeUseCase: GetEmployeeUseCase
) : ViewModel() {

    private companion object {
        private const val TAG = "EmployeeListViewModel"
    }

    private val _uiState = MutableStateFlow(EmployeeListUiState())
    val uiState: StateFlow<EmployeeListUiState> = _uiState

    private var errorLabel: String = ""

    fun bindLabel(error: String) {
        errorLabel = error
    }

    val employeeListPaged : Flow<PagingData<Employee>> = getEmployeeUseCase()
        .cachedIn(viewModelScope)

    fun onEvent(event: EmployeeListUiEvent) {
        when (event) {
            is EmployeeListUiEvent.LoadEmployeeList -> {}
            is EmployeeListUiEvent.RetryEmployeeList -> {}
            is EmployeeListUiEvent.ShowEditBottomSheet -> _uiState.update { it.copy(selectedEmployee = event.employee, bottomSheetType = EmployeeBottomSheetType.EDIT) }
            is EmployeeListUiEvent.ShowStatusBottomSheet -> _uiState.update { it.copy(selectedEmployee = event.employee, bottomSheetType = EmployeeBottomSheetType.STATUS) }
            is EmployeeListUiEvent.DismissBottomSheet -> _uiState.update { it.copy(selectedEmployee = null) }
        }
    }
}