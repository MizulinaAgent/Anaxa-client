package com.example.anaxa.ui.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

fun relativeDays(createdAt: String): String {
    val date = runCatching { LocalDateTime.parse(createdAt).toLocalDate() }
        .getOrElse { runCatching { LocalDate.parse(createdAt.take(10)) }.getOrNull() }
        ?: return ""

    val days = ChronoUnit.DAYS.between(date, LocalDate.now()).toInt()
    return when {
        days <= 0 -> "сегодня"
        days == 1 -> "вчера"
        days in 2..4 -> "$days дня назад"
        else -> "$days дней назад"
    }
}
