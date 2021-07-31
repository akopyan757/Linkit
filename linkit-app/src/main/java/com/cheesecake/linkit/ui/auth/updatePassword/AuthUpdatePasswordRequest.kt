package com.cheesecake.linkit.ui.auth.updatePassword

data class AuthUpdatePasswordRequest(
    val oldPassword: String,
    val newPassword: String,
    val passwordConfirm: String
)
