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
    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state

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
            _state.value = _state.value.copy(email = e.email)
            validateEmail()
        }
        is LoginUiEvent.PasswordChanged -> {
            _state.value = _state.value.copy(password = e.password)
            validatePassword()
        }
        LoginUiEvent.Submit -> submit()
    }

    private fun validateEmail() {
        val result = AuthValidator.validateNotEmpty(_state.value.email, emailLabel)
        _state.value = _state.value.copy(
            emailError = result.toErrorMessage()
        )
    }

    private fun validatePassword() {
        val result = AuthValidator.validateNotEmpty(_state.value.password, passwordLabel)
        _state.value = _state.value.copy(
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

        if (!_state.value.isValid) return@launch

        val s = _state.value
        _state.update { it.copy(loading = true, error = null) }
        runCatching { singIn(s.email, s.password) }
            .onSuccess { _state.update { it.copy(loading = false, success = true) } }
            .onFailure { throwable ->
                val backendMessage = throwable.serverMessageOrNull()
                _state.update {
                    it.copy(loading = false, error = backendMessage ?: (throwable.message ?: errorLabel))
                }
            }
        Log.d("LoginViewModel", "submit: ${_state.value}")
    }

    fun consumeError() {
        _state.update { it.copy(error = null) }
    }
}