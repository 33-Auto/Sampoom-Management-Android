package com.sampoom.android.feature.auth.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sampoom.android.R
import com.sampoom.android.core.network.serverMessageOrNull
import com.sampoom.android.core.util.AuthValidator
import com.sampoom.android.core.util.AuthValidator.validateEmail
import com.sampoom.android.core.util.AuthValidator.validatePassword
import com.sampoom.android.core.util.AuthValidator.validatePasswordCheck
import com.sampoom.android.core.util.GlobalMessageHandler
import com.sampoom.android.core.util.ValidationResult
import com.sampoom.android.feature.auth.domain.usecase.GetVendorUseCase
import com.sampoom.android.feature.auth.domain.usecase.SignUpUseCase
import com.sampoom.android.feature.user.domain.usecase.GetProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val messageHandler: GlobalMessageHandler,
    private val singUp: SignUpUseCase,
    private val getVendorUseCase: GetVendorUseCase,
    private val getProfileUseCase: GetProfileUseCase,
    private val application: Application
) : ViewModel() {

    private companion object {
        private const val TAG = "SignUpViewModel"
    }

    private val _state = MutableStateFlow(SignUpUiState())
    val state: StateFlow<SignUpUiState> = _state

    private var nameLabel: String = ""
    private var branchLabel: String = ""
    private var positionLabel: String = ""
    private var errorLabel: String = ""

    init {
        loadVendors()
    }

    fun bindLabels(name: String, branch: String, position: String, error: String) {
        nameLabel = name
        branchLabel = branch
        positionLabel = position
        errorLabel = error
    }

    fun onEvent(e: SignUpUiEvent) = when (e) {
        is SignUpUiEvent.NameChanged -> {
            _state.value = _state.value.copy(name = e.name)
            validateName()
        }
        is SignUpUiEvent.VendorChanged -> {
            _state.value = _state.value.copy(
                selectedVendor = e.vendor,
                branch = e.vendor.name
            )
            validateBranch()
        }
        is SignUpUiEvent.PositionChanged -> {
            _state.value = _state.value.copy(position = e.position)
            validatePosition()
        }
        is SignUpUiEvent.EmailChanged -> {
            _state.value = _state.value.copy(email = e.email)
            validateEmail()
        }
        is SignUpUiEvent.PasswordChanged -> {
            _state.value = _state.value.copy(password = e.password)
            validatePassword()
            if (_state.value.passwordCheck.isNotBlank()) {
                validatePasswordCheck()
            } else { }
        }
        is SignUpUiEvent.PasswordCheckChanged -> {
            _state.value = _state.value.copy(passwordCheck = e.passwordCheck)
            validatePasswordCheck()
        }
        SignUpUiEvent.Submit -> submit()
    }

    private fun validateName() {
        val result = AuthValidator.validateNotEmpty(_state.value.name, nameLabel)
        _state.value = _state.value.copy(
            nameError = result.toErrorMessage()
        )
    }

    private fun validateBranch() {
        val result = AuthValidator.validateNotEmpty(_state.value.branch, branchLabel)
        _state.value = _state.value.copy(
            branchError = result.toErrorMessage()
        )
    }

    private fun validatePosition() {
        val result = if (_state.value.position == null) {
            ValidationResult.Error(messageResId = R.string.common_required_field)
        } else {
            ValidationResult.Success
        }
        _state.value = _state.value.copy(
            positionError = result.toErrorMessage()
        )
    }

    private fun validateEmail() {
        val result = validateEmail(_state.value.email)
        _state.value = _state.value.copy(
            emailError = result.toErrorMessage()
        )
    }

    private fun validatePassword() {
        val result = validatePassword(_state.value.password)
        _state.value = _state.value.copy(
            passwordError = result.toErrorMessage()
        )
    }

    private fun validatePasswordCheck() {
        val result = validatePasswordCheck(_state.value.password, _state.value.passwordCheck)
        _state.value = _state.value.copy(
            passwordCheckError = result.toErrorMessage()
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
        validateName()
        validateBranch()
        validatePosition()
        validateEmail()
        validatePassword()
        validatePasswordCheck()

        if (!_state.value.isValid) return@launch

        val s = _state.value
        _state.update { it.copy(loading = true) }
        singUp(
            email = s.email,
            password = s.password,
            workspace = s.workspace,
            branch = s.branch,
            userName = s.name,
            position = s.position!!.name
        )
            .onSuccess {
                getProfileUseCase("AGENCY")
                    .onSuccess {
                        _state.update {
                            it.copy(loading = false, success = true)
                        }
                    }
                    .onFailure { throwable ->
                        val backendMessage = throwable.serverMessageOrNull()
                        val error = backendMessage ?: (throwable.message ?: errorLabel)
                        messageHandler.showMessage(message = error, isError = true)

                        _state.update {
                            it.copy(loading = false, success = false)
                        }
                    }
            }
            .onFailure { throwable ->
                val backendMessage = throwable.serverMessageOrNull()
                val error = backendMessage ?: (throwable.message ?: errorLabel)
                messageHandler.showMessage(message = error, isError = true)

                _state.update {
                    it.copy(loading = false, )
                }
            }
        Log.d(TAG, "submit: ${_state.value}")
    }

    private fun loadVendors() {
        viewModelScope.launch {
            _state.update { it.copy(vendorsLoading = true) }
            getVendorUseCase()
                .onSuccess { vendorList ->
                    _state.update {
                        it.copy(
                            vendors = vendorList.items,
                            vendorsLoading = false
                        )
                    }
                }
                .onFailure { throwable ->
                    val backendMessage = throwable.serverMessageOrNull()
                    val error = backendMessage ?: (throwable.message ?: errorLabel)
                    messageHandler.showMessage(message = error, isError = true)
                    _state.update { it.copy(vendorsLoading = false) }
                }
        }
    }
}
