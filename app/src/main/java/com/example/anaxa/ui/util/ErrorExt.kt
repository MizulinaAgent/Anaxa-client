package com.example.anaxa.ui.util

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import retrofit2.HttpException
import java.io.IOException

private val errorJson = Json { ignoreUnknownKeys = true }

private fun HttpException.serverMessage(): String? = runCatching {
    val body = response()?.errorBody()?.string()
    if (body.isNullOrBlank()) return null
    errorJson.parseToJsonElement(body).jsonObject["error"]?.jsonPrimitive?.content
}.getOrNull()

fun Throwable.toUserMessage(): String = when (this) {
    is IOException -> "Нет соединения с сервером"
    is HttpException -> serverMessage() ?: when (code()) {
        400 -> "Проверьте введённые данные"
        401 -> "Неверный email или пароль"
        403 -> "Нет доступа"
        404 -> "Не найдено"
        409 -> "Конфликт данных"
        else -> "Ошибка сервера (${code()})"
    }
    else -> "Что-то пошло не так"
}
