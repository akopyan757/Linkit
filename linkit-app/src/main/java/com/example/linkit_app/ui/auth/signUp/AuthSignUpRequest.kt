package com.example.linkit_app.ui.auth.signUp

data class AuthSignUpRequest(
    val email: String,
    val password: String,
    val passwordConfirm: String
)
