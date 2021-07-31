package com.cheesecake.linkit.ui.auth.setPassword

data class AuthSetPasswordRequest(
    val newPassword: String,
    val passwordConfirm: String
)
