package com.cheesecake.linkit.ui.auth.signUp

data class AuthSignUpRequest(
    val email: String,
    val password: String,
    val passwordConfirm: String
)
