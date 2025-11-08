package com.sampoom.android.feature.user.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sampoom.android.core.model.EmployeeStatus
import com.sampoom.android.core.network.serverMessageOrNull
import com.sampoom.android.core.util.GlobalMessageHandler
import com.sampoom.android.feature.user.domain.usecase.UpdateEmployeeStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateEmployeeStatusViewModel @Inject constructor(
    private val messageHandler: GlobalMessageHandler,
    private val updateEmployeeStatusUseCase: UpdateEmployeeStatusUseCase
) : ViewModel() {

    private companion object {
        private const val TAG = "UpdateEmployeeStatusViewModel"
    }

    private val _uiState = MutableStateFlow(UpdateEmployeeStatusUiState())
    val uiState: StateFlow<UpdateEmployeeStatusUiState> = _uiState

    private var errorLabel: String = ""
    private var editEmployeeLabel: String = ""

    fun bindLabel(error: String, editEmployee: String) {
        errorLabel = error
        editEmployeeLabel = editEmployee
    }

    fun onEvent(event: UpdateEmployeeStatusUiEvent) {
        when (event) {
            is UpdateEmployeeStatusUiEvent.Initialize -> {
                _uiState.update {
                    it.copy(
                        employee = event.employee,
                        isLoading = false,
                        isSuccess = false
                    )
                }
            }
            is UpdateEmployeeStatusUiEvent.EditEmployeeStatus -> {
                editEmployeeStatus(event.employeeStatus)
            }
            is UpdateEmployeeStatusUiEvent.Dismiss -> {
                _uiState.update {
                    it.copy(
                        employee = null,
                        isLoading = false,
                        error = null
                    )
                }
            }
        }
    }

    private fun editEmployeeStatus(newEmployeeStatus: EmployeeStatus) {
        viewModelScope.launch {
            val currentEmployee = _uiState.value.employee ?: run {
                messageHandler.showMessage(message = errorLabel, isError = true)
                return@launch
            }

            val updateEmployee = currentEmployee.copy(employeeStatus = newEmployeeStatus)
            _uiState.update { it.copy(isLoading = true, error = null) }

            updateEmployeeStatusUseCase(updateEmployee, "AGENCY")
                .onSuccess { employee ->
                    _uiState.update {
                        it.copy(
                            employee = employee,
                            isLoading = false,
                            error = null,
                            isSuccess = true
                        )
                    }
                    messageHandler.showMessage(message = editEmployeeLabel, isError = false)
                }
                .onFailure { throwable ->
                    val backendMessage = throwable.serverMessageOrNull()
                    val error = backendMessage ?: (throwable.message ?: errorLabel)
                    messageHandler.showMessage(message = error, isError = true)

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error
                        )
                    }
                }
        }
    }

    fun clearStatus() {
        _uiState.update { it.copy(isSuccess = false, error = null) }
    }
}