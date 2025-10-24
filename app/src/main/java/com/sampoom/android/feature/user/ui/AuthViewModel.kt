package com.sampoom.android.feature.user.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sampoom.android.feature.user.domain.usecase.CheckLoginStateUseCase
import com.sampoom.android.feature.user.domain.usecase.ClearTokensUseCase
import com.sampoom.android.feature.user.domain.usecase.SignOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val checkLoginStateUseCase: CheckLoginStateUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val clearTokensUseCase: ClearTokensUseCase
) : ViewModel() {
    private val _isLoggedIn = MutableStateFlow(checkLoginStateUseCase())
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _logoutEvent = MutableSharedFlow<Unit>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val logoutEvent: SharedFlow<Unit> = _logoutEvent.asSharedFlow()

    fun updateLoginState() {
        _isLoggedIn.value = checkLoginStateUseCase()
    }

    fun signOut() = viewModelScope.launch {
        signOutUseCase()
        _isLoggedIn.value = false
        _logoutEvent.emit(Unit)
    }

    fun handleTokenExpired() = viewModelScope.launch {
        clearTokensUseCase()
        _isLoggedIn.value = false
        _logoutEvent.emit(Unit)
    }
}