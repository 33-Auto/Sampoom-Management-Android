package com.sampoom.android.core.util

fun formatDate(dateString: String): String {
    return try {
        val inFmt = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", java.util.Locale.getDefault())
        val outFmt = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
        outFmt.format(inFmt.parse(dateString) ?: java.util.Date())
    } catch (_: Exception) {
        dateString
    }
}