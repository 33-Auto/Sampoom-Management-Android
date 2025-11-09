package com.sampoom.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sampoom.android.core.util.GlobalMessageHandler
import com.sampoom.android.feature.user.domain.model.User
import com.sampoom.android.feature.user.domain.usecase.GetStoredUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    val messageHandler: GlobalMessageHandler,
    private val getStoredUserUseCase: GetStoredUserUseCase
) : ViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    init {
        viewModelScope.launch {
            _user.value = getStoredUserUseCase()
        }
    }
}