package com.sampoom.android.core.util

import kotlinx.coroutines.delay
import java.io.IOException
import java.net.SocketTimeoutException

suspend fun <T> retry(
    times: Int = 5,
    initialDelay: Long = 300,
    maxDelay: Long = 1500,
    factor: Double = 1.8,
    block: suspend () -> T
): T {
    var currentDelay = initialDelay
    repeat(times - 1) {
        try {
            return block()
        } catch (t: Throwable) {
            when (t) {
                is SocketTimeoutException, is IOException -> {

                }
                else -> throw t
            }
        }
        delay(currentDelay)
        currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelay)
    }
    return block()
}