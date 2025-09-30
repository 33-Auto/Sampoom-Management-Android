package com.sampoom.android.feature.auth.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sampoom.android.feature.auth.domain.usecase.SignInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val singIn: SignInUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state

    fun onEvent(e: LoginUiEvent) = when (e) {
        is LoginUiEvent.EmailChanged -> _state.value = _state.value.copy(email = e.email)
        is LoginUiEvent.PasswordChanged -> _state.value = _state.value.copy(password = e.password)
        LoginUiEvent.Submit -> submit()
    }

    private fun submit() = viewModelScope.launch {
        val s = _state.value
        _state.update { it.copy(loading = true, error = null) }
        runCatching { singIn(s.email, s.password) }
            .onSuccess { _state.update { it.copy(loading = false, success = true) } }
            .onFailure { _state.update { it.copy(loading = false, error = it.error) } }
        Log.d("LoginViewModel", "submit: ${_state.value}")
    }
}