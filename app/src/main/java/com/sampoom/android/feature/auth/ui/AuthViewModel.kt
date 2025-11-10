package com.sampoom.android.feature.auth.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sampoom.android.core.network.TokenLogoutEmitter
import com.sampoom.android.feature.auth.domain.usecase.CheckLoginStateUseCase
import com.sampoom.android.feature.auth.domain.usecase.ClearTokensUseCase
import com.sampoom.android.feature.auth.domain.usecase.SignOutUseCase
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
    private val clearTokensUseCase: ClearTokensUseCase,
    tokenLogoutEmitter: TokenLogoutEmitter
) : ViewModel() {
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _logoutEvent = MutableSharedFlow<Unit>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val logoutEvent: SharedFlow<Unit> = _logoutEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            _isLoggedIn.value = checkLoginStateUseCase()
            _isLoading.value = false
            tokenLogoutEmitter.events.collect {
                signOut()
            }
        }
    }

    fun updateLoginState() = viewModelScope.launch {
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