package com.example.linkit_app.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.linkit_app.ui.theme.*

@Composable
fun InputItemLayout(
    value: String,
    setTextChanged: (String) -> Unit,
    hint: String,
    error: String = "",
    errorState: Boolean,
    modifier: Modifier
) {

    Column(
        modifier = modifier
    ) {
        TextField(
            value = value,
            onValueChange = setTextChanged,
            label = { Text(text = hint) },
            maxLines = 1,
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Surface,
                //focusedLabelColor = Disabled,
                //unfocusedLabelColor = Disabled,
                errorIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            isError = errorState
        )

        if (error.isNotEmpty()) {
            Text(
                text = error,
                style = MaterialTheme.typography.overline,
                color = Error,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp)
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun InputItemLayoutPreview() {
    LinkitTheme {
        val text = remember { mutableStateOf("Hello!") }
        val error = remember { mutableStateOf("") }
        val errorState = remember { mutableStateOf(false) }
        InputItemLayout(
            value = text.value,
            setTextChanged = { value ->
                text.value = value
                error.value = if (value.isEmpty()) "Error" else ""
                errorState.value = value.isEmpty()
            },
            hint = "Hint",
            error = error.value,
            errorState = errorState.value,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(24.dp)
        )
    }
}

@Composable
@Preview(showBackground = true)
fun InputItemErrorLayoutPreview() {
    LinkitTheme {
        val text = remember { mutableStateOf("Hello!") }
        val error = remember { mutableStateOf("Error") }
        val errorState = remember { mutableStateOf(true) }
        InputItemLayout(
            value = text.value,
            setTextChanged = { value ->
                text.value = value
                error.value = if (value.isEmpty()) "Error" else ""
                errorState.value = value.isEmpty()
            },
            hint = "Hint",
            error = error.value,
            errorState = errorState.value,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(24.dp)
        )
    }
}