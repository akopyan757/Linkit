package com.cheesecake.linkit.ui.auth.splash

import com.akopyan757.linkit_domain.usecase.auth.GetUserUseCase
import com.cheesecake.linkit.compose.BaseViewModel

class AuthSplashViewModel : BaseViewModel() {

    private val getUser: GetUserUseCase by injectUseCase()

    private var state = State.Loading

    fun requestUser(onFinished: (State) -> Unit) {
        if (state == State.Loading) {
            getUser.execute(onSuccess = {
                state = State.UserAuthorized
                onFinished.invoke(state)
            }, onError = {
                state = State.UserNotFound
                onFinished.invoke(state)
            })
        }
    }

    enum class State { Loading, UserAuthorized, UserNotFound }
}