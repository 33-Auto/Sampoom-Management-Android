package com.sampoom.android.core.util

import kotlinx.coroutines.delay

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
            // 서버 반영 지연에서만 재시도하고, 다른 오류는 그대로 throw 하려면 필터링 가능
        }
        delay(currentDelay)
        currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelay)
    }
    return block()
}