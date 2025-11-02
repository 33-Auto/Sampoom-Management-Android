package com.sampoom.android.core.util

import com.sampoom.android.core.model.SnackBarMessage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GlobalMessageHandler @Inject constructor() {
    private val _message = MutableStateFlow<SnackBarMessage?>(null)
    val message: StateFlow<SnackBarMessage?> = _message.asStateFlow()

    suspend fun showMessage(message: String, isError: Boolean = false) {
        _message.value = SnackBarMessage(message, isError)
        delay(3000)
        _message.value = null
    }
}