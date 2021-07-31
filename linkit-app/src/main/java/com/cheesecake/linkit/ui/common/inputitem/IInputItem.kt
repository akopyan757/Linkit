package com.cheesecake.linkit.ui.common.inputitem

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cheesecake.linkit.ui.common.inputitem.checkOptions.BaseCheckOption
import com.cheesecake.linkit.ui.theme.Black
import com.cheesecake.linkit.ui.theme.Error
import com.cheesecake.linkit.ui.theme.Grey
import com.cheesecake.linkit.ui.theme.Surface

open class IInputItem(private val id: String) {

    var label: String = ""
    var maxLines = 1
    var errorColor: Color = Error
    var tintColor: Color = Grey
    var onTextChanged: (String) -> Unit = {}
    var checkOptions = mutableListOf<BaseCheckOption>()

    private val text = MutableLiveData<String>()
    private val error = MutableLiveData<Boolean>()

    protected open val keyboardType = KeyboardType.Text
    protected open val imeAction: ImeAction = ImeAction.Next

    fun setError(state: Boolean) {
        error.value = state
    }

    fun getErrorState(): LiveData<Boolean> = error
    fun getInputItemValue() = text.value ?: ""

    open fun getInputItemId() = id

    @Composable protected open fun getVisualTransformation() = VisualTransformation.None
    @Composable protected open fun GetTrailingIcon() {}

    open fun getErrorCheckOptions(inputItems: List<IInputItem>, delayCheck: Boolean): List<BaseCheckOption> {
        return checkOptions.filter { checkOption ->
            if (!checkOption.isOk(this, inputItems, delayCheck)) {
                !checkOption.mandatory || !delayCheck
            } else false
        }
    }

    @Composable
    fun getInputLayout() {
        val focusManager = LocalFocusManager.current
        val textState: String by text.observeAsState("")
        val errorState: Boolean by getErrorState().observeAsState(false)
        TextField(
            value = textState,
            onValueChange = {
                text.value = it
                onTextChanged.invoke(it)
            },
            label = { Text(label) },
            maxLines = maxLines,
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Surface,
                cursorColor = Black,
                errorIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedLabelColor = Grey,
                unfocusedLabelColor = Grey
            ),
            visualTransformation = getVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            keyboardActions = KeyboardActions(onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            }),
            trailingIcon = { GetTrailingIcon() },
            isError = errorState,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )
    }
}