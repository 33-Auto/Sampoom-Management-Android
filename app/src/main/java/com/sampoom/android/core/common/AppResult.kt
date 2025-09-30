package com.sampoom.android.core.common

sealed class AppResult<out T> {
    data class Success<T>(val value: T) : AppResult<T>()
    data class Error(val throwable: Throwable) : AppResult<Nothing>()
}

inline fun <T> appRunCatching(block: () -> T): AppResult<T> = try {
    AppResult.Success(block())
} catch (t: Throwable) {
    AppResult.Error(t)
}