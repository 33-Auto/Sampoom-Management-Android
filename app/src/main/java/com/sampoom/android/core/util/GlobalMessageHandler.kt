package com.sampoom.android.core.util

import com.sampoom.android.core.model.SnackBarMessage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GlobalMessageHandler @Inject constructor() {
    private val _message = MutableStateFlow<SnackBarMessage?>(null)
    val message: SharedFlow<SnackBarMessage?> = _message.asSharedFlow()

    suspend fun showMessage(message: String, isError: Boolean = false) {
        _message.value = SnackBarMessage(message, isError)
        delay(100)
        _message.value = null
    }
}