package com.sampoom.android.feature.user.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sampoom.android.core.model.UserPosition
import com.sampoom.android.core.network.serverMessageOrNull
import com.sampoom.android.core.util.GlobalMessageHandler
import com.sampoom.android.feature.user.domain.usecase.EditEmployeeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditEmployeeViewModel @Inject constructor(
    private val messageHandler: GlobalMessageHandler,
    private val editEmployeeUseCase: EditEmployeeUseCase
) : ViewModel() {

    private companion object {
        private const val TAG = "EditEmployeeViewModel"
    }

    private val _uiState = MutableStateFlow(EditEmployeeUiState())
    val uiState: StateFlow<EditEmployeeUiState> = _uiState

    private var errorLabel: String = ""
    private var editEmployeeLabel: String = ""

    fun bindLabel(error: String, editEmployee: String) {
        errorLabel = error
        editEmployeeLabel = editEmployee
    }

    fun onEvent(event: EditEmployeeUiEvent) {
        when (event) {
            is EditEmployeeUiEvent.Initialize -> {
                _uiState.update {
                    it.copy(
                        employee = event.employee,
                        isLoading = false,
                        isSuccess = false
                    )
                }
            }
            is EditEmployeeUiEvent.EditEmployee -> {
                editEmployee(event.position)
            }
            is EditEmployeeUiEvent.Dismiss -> {
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

    private fun editEmployee(newPosition: UserPosition) {
        viewModelScope.launch {
            val currentEmployee = _uiState.value.employee ?: run {
                messageHandler.showMessage(message = errorLabel, isError = true)
                return@launch
            }

            val updateEmployee = currentEmployee.copy(position = newPosition)
            _uiState.update { it.copy(isLoading = true, error = null) }

            editEmployeeUseCase(updateEmployee, "AGENCY")
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