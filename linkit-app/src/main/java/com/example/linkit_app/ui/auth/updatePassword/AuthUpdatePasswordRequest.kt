package com.example.linkit_app.ui.auth.updatePassword

data class AuthUpdatePasswordRequest(
    val oldPassword: String,
    val newPassword: String,
    val passwordConfirm: String
)
