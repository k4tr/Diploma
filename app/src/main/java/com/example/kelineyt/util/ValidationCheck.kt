package com.example.kelineyt.util

import android.util.Patterns

fun validateEmail(email: String): RegisterValidation{
    if (email.isEmpty())
        return RegisterValidation.Failed("Вы не ввели почту")

    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        return RegisterValidation.Failed("Неверный формат почты")

    return RegisterValidation.Success
}

fun validatePassword(password: String): RegisterValidation{
    if (password.isEmpty())
        return RegisterValidation.Failed("Вы не ввели пароль")

    if (password.length < 6)
        return RegisterValidation.Failed("Пароль должен содержать не менее 6 символов")

    return RegisterValidation.Success
}