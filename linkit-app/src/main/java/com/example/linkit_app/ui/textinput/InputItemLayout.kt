package com.example.linkit_app.ui.textinput

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
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
    error: String,
    errorState: Boolean
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp)
    ) {
        TextField(
            value = value,
            onValueChange = setTextChanged,
            label = { Text(text = hint) },
            maxLines = 1,
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = BackgroundTransparent,
                focusedLabelColor = Disabled,
                unfocusedLabelColor = Disabled,
                cursorColor = Black,
                errorCursorColor = Black,
                errorLabelColor = Error,
                errorLeadingIconColor = Error,
                errorIndicatorColor = Error,
                placeholderColor = Color.Transparent,
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

@Preview
@Composable
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
            errorState = errorState.value
        )
    }
}