package com.example.linkit_app.ui.textinput

import java.io.Serializable

data class InputItem(
    val value: String,
    val setTextChanged: (String) -> Unit,
    val hint: String,
    val error: String,
    val errorState: Boolean
) : Serializable
