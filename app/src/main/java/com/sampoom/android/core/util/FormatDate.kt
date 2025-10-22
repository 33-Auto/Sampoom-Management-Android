package com.sampoom.android.core.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun formatDate(dateString: String): String {
    return runCatching {
        val out = DateTimeFormatter.ISO_LOCAL_DATE
        val hasOffset =
            dateString.contains("Z") || dateString.lastIndexOf('+') > dateString.indexOf('T') || (dateString.lastIndexOf(
                '-'
            ) > dateString.indexOf('T') && dateString.count { it == ':' } >= 3)
        val date = if (hasOffset) {
            val inFmt = DateTimeFormatter.ISO_OFFSET_DATE_TIME
            java.time.OffsetDateTime.parse(dateString, inFmt).toLocalDate()
        } else {
            val inFmt = java.time.format.DateTimeFormatterBuilder()
                .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
                .optionalStart()
                .appendFraction(java.time.temporal.ChronoField.NANO_OF_SECOND, 0, 6, true)
                .optionalEnd()
                .toFormatter(java.util.Locale.ROOT)
            java.time.LocalDateTime.parse(dateString, inFmt).toLocalDate()
        }
        date.format(out)
    }.getOrElse { dateString }
}