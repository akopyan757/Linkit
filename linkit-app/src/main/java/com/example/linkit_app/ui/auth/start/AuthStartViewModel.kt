package com.example.linkit_app.ui.auth.start

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.linkit_app.ui.common.inputitem.checkOptions.BaseCheckOption
import com.example.linkit_app.ui.common.inputitem.EmailInputItem
import com.example.linkit_app.ui.common.inputitem.PasswordInputItem
import com.example.linkit_app.ui.common.inputitem.checkOptions.PasswordEqualsCheckOption
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AuthStartViewModel : ViewModel() {

    val progressVisibility = MutableLiveData(false)
}