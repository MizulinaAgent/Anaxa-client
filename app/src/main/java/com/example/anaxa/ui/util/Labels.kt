package com.example.anaxa.ui.util

fun categoryLabel(type: String): String = when (type) {
    "currency" -> "Валюта"
    "items" -> "Предметы"
    "accounts" -> "Аккаунты"
    "services" -> "Услуги"
    else -> type
}

fun lotStatusLabel(status: String): String = when (status) {
    "active" -> "Активен"
    "sold" -> "Продан"
    "hidden" -> "Скрыт"
    else -> status
}

fun orderStatusLabel(status: String): String = when (status) {
    "pending" -> "Ожидает"
    "paid" -> "Оплачен"
    "completed" -> "Завершён"
    "cancelled" -> "Отменён"
    else -> status
}
