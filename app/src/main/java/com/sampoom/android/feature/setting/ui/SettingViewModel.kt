package com.sampoom.android.feature.setting.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sampoom.android.core.util.GlobalMessageHandler
import com.sampoom.android.feature.auth.domain.AuthValidator
import com.sampoom.android.feature.auth.domain.ValidationResult
import com.sampoom.android.feature.auth.domain.model.User
import com.sampoom.android.feature.auth.domain.usecase.GetStoredUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val messageHandler: GlobalMessageHandler,
    private val application: Application,
    private val getStoredUserUseCase: GetStoredUserUseCase
) : ViewModel() {

    private companion object {
        private const val TAG = "SettingViewModel"
    }

    private val _uiState = MutableStateFlow(SettingUiState())
    val uiState: StateFlow<SettingUiState> = _uiState

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private var nameLabel: String = ""
    private var errorLabel: String = ""

    fun bindLabel(name: String, error: String) {
        nameLabel = name
        errorLabel = error
    }

    init {
        viewModelScope.launch {
            _user.value = getStoredUserUseCase()
        }
    }

    fun onEvent(event: SettingUiEvent) {
        when (event) {
            is SettingUiEvent.LoadProfile -> {}
            is SettingUiEvent.NameChanged -> {
                _uiState.value = _uiState.value.copy(userName = event.userName)
                validateName()
            }
            is SettingUiEvent.EditProfile -> editProfile()
        }
    }

    private fun validateName() {
        val result = AuthValidator.validateNotEmpty(_uiState.value.userName, nameLabel)
        _uiState.value = _uiState.value.copy(
            userNameError = result.toErrorMessage()
        )
    }

    private fun ValidationResult.toErrorMessage(): String? {
        return when (this) {
            is ValidationResult.Error -> application.getString(messageResId)
            is ValidationResult.ErrorWithArgs -> application.getString(messageResId, args)
            ValidationResult.Success -> null
        }
    }

    private fun editProfile() = viewModelScope.launch {
        validateName()

        if (!_uiState.value.isValid) return@launch

        val s = _uiState.value
        _uiState.update { it.copy(loading = true) }

        // TODO : Edit Profile 연동
    }

    fun clearSuccess() {
        _uiState.update { it.copy(profileChangeSuccess = false, logoutSuccess = false) }
    }
}