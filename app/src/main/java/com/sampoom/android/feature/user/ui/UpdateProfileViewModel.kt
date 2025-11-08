package com.sampoom.android.feature.user.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sampoom.android.core.network.serverMessageOrNull
import com.sampoom.android.core.util.GlobalMessageHandler
import com.sampoom.android.feature.user.domain.usecase.UpdateProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateProfileViewModel @Inject constructor(
    private val messageHandler: GlobalMessageHandler,
    private val updateProfileUseCase: UpdateProfileUseCase
) : ViewModel() {

    private companion object Companion {
        private const val TAG = "UpdateProfileViewModel"
    }

    private val _uiState = MutableStateFlow(UpdateProfileUiState())
    val uiState: StateFlow<UpdateProfileUiState> = _uiState

    private var errorLabel: String = ""
    private var updateProfileLabel: String = ""

    fun bindLabel(error: String, updateProfile: String) {
        errorLabel = error
        updateProfileLabel = updateProfile
    }

    fun onEvent(event: UpdateProfileUiEvent) {
        when (event) {
            is UpdateProfileUiEvent.Initialize -> {
                _uiState.update {
                    it.copy(
                        user = event.user,
                        isLoading = false,
                        isSuccess = false
                    )
                }
            }
            is UpdateProfileUiEvent.UpdateProfile -> {
                updateProfile(event.userName)
            }
            is UpdateProfileUiEvent.Dismiss -> {
                _uiState.update {
                    it.copy(
                        user = null
                    )
                }
            }
        }
    }

    private fun updateProfile(newUserName: String) {
        viewModelScope.launch {
            val currentUser = _uiState.value.user ?: run {
                messageHandler.showMessage(message = errorLabel, isError = true)
                return@launch
            }

            val updatedUser = currentUser.copy(userName = newUserName)
            _uiState.update { it.copy(isLoading = true, error = null) }

            updateProfileUseCase(updatedUser)
                .onSuccess { user ->
                    _uiState.update {
                        it.copy(
                            user = user,
                            isLoading = false,
                            error = null,
                            isSuccess = true
                        )
                    }
                    messageHandler.showMessage(message = updateProfileLabel, isError = false)
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