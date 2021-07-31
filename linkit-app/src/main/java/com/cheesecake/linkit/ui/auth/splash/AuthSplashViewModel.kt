package com.cheesecake.linkit.ui.auth.splash

import com.akopyan757.linkit_domain.usecase.auth.GetUserUseCase
import com.cheesecake.linkit.compose.BaseViewModel

class AuthSplashViewModel : BaseViewModel() {

    private val getUser: GetUserUseCase by injectUseCase()

    private var state: State = State.Loading

    fun requestUser(onFinished: (State) -> Unit) {
        if (state == State.Loading) {
            getUser.execute(onSuccess = { entity ->
                state = State.UserAuthorized(entity.uid)
                onFinished.invoke(state)
            }, onError = {
                state = State.UserNotFound
                onFinished.invoke(state)
            })
        }
    }

    sealed class State {
        object Loading: State()
        object UserNotFound: State()
        data class UserAuthorized(val uid: String): State()
    }
}