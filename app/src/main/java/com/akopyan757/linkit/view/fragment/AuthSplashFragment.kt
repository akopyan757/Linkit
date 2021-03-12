package com.akopyan757.linkit.view.fragment

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.akopyan757.base.view.BaseFragment
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.R
import com.akopyan757.linkit.databinding.FragmentSplashBinding
import com.akopyan757.linkit.viewmodel.AuthSplashViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent

class AuthSplashFragment: BaseFragment<FragmentSplashBinding, AuthSplashViewModel>(), KoinComponent {

    override val mViewModel: AuthSplashViewModel by viewModel()

    override fun getLayoutId() = R.layout.fragment_splash
    override fun getVariableId() = BR.viewModel

    override fun onSetupView(bundle: Bundle?) {
        receiveUserAccount()
    }

    private fun receiveUserAccount() {
        mViewModel.requestGetUser().apply {
            observeSuccessResponse { firebaseUser -> openMainScreen(firebaseUser.uid) }
            observeErrorResponse { openSignInScreen() }
        }
    }

    private fun openSignInScreen() {
        findNavController().navigate(R.id.action_splashFragment_to_authSignInFragment)
    }
}