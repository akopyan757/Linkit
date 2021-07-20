package com.example.linkit_app.ui.authSignUp

data class AuthSignUpRequest(
    val email: String,
    val password: String,
    val passwordConfirm: String
)
