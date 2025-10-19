package com.sampoom.android.feature.auth.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sampoom.android.feature.auth.domain.AuthValidator
import com.sampoom.android.feature.auth.domain.ValidationResult
import com.sampoom.android.feature.auth.domain.usecase.LoginUseCase
import com.sampoom.android.core.network.serverMessageOrNull
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val singIn: LoginUseCase,
    private val application: Application
) : ViewModel() {

    private companion object {
        private const val TAG = "LoginViewModel"
    }

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    private var emailLabel: String = ""
    private var passwordLabel: String = ""
    private var errorLabel: String = ""

    fun bindLabel(email: String, password: String, error: String) {
        emailLabel = email
        passwordLabel = password
        errorLabel = error
    }

    fun onEvent(e: LoginUiEvent) = when (e) {
        is LoginUiEvent.EmailChanged -> {
            _uiState.value = _uiState.value.copy(email = e.email)
            validateEmail()
        }
        is LoginUiEvent.PasswordChanged -> {
            _uiState.value = _uiState.value.copy(password = e.password)
            validatePassword()
        }
        LoginUiEvent.Submit -> submit()
    }

    private fun validateEmail() {
        val result = AuthValidator.validateNotEmpty(_uiState.value.email, emailLabel)
        _uiState.value = _uiState.value.copy(
            emailError = result.toErrorMessage()
        )
    }

    private fun validatePassword() {
        val result = AuthValidator.validateNotEmpty(_uiState.value.password, passwordLabel)
        _uiState.value = _uiState.value.copy(
            passwordError = result.toErrorMessage()
        )
    }

    private fun ValidationResult.toErrorMessage(): String? {
        return when (this) {
            is ValidationResult.Error -> application.getString(messageResId)
            is ValidationResult.ErrorWithArgs -> application.getString(messageResId, args)
            ValidationResult.Success -> null
        }
    }

    private fun submit() = viewModelScope.launch {
        validateEmail()
        validatePassword()

        if (!_uiState.value.isValid) return@launch

        val s = _uiState.value
        _uiState.update { it.copy(loading = true, error = null) }
        runCatching { singIn(s.email, s.password) }
            .onSuccess { _uiState.update { it.copy(loading = false, success = true) } }
            .onFailure { throwable ->
                val backendMessage = throwable.serverMessageOrNull()
                _uiState.update {
                    it.copy(loading = false, error = backendMessage ?: (throwable.message ?: errorLabel))
                }
            }
        Log.d(TAG, "submit: ${_uiState.value}")
    }

    fun consumeError() {
        _uiState.update { it.copy(error = null) }
    }
}