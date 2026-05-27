package com.example.anaxa.ui.util

import retrofit2.HttpException
import java.io.IOException

fun Throwable.toUserMessage(): String = when (this) {
    is IOException -> "Нет соединения с сервером"
    is HttpException -> when (code()) {
        400 -> "Проверьте введённые данные"
        401 -> "Неверный email или пароль"
        403 -> "Нет доступа"
        404 -> "Не найдено"
        409 -> "Email уже зарегистрирован"
        else -> "Ошибка сервера (${code()})"
    }
    else -> "Что-то пошло не так"
}
