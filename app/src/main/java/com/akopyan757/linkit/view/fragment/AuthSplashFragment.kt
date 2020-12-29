package com.akopyan757.linkit.view.fragment

import android.content.Intent
import android.util.Log
import androidx.navigation.fragment.findNavController
import com.akopyan757.base.view.BaseFragment
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.R
import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit.databinding.FragmentSplashBinding
import com.akopyan757.linkit.view.MainActivity
import com.akopyan757.linkit.viewmodel.AuthSplashViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent

class AuthSplashFragment: BaseFragment<FragmentSplashBinding, AuthSplashViewModel>(), KoinComponent {

    companion object {
        private const val TAG = "AUTH_SPLASH_FRAGMENT"
    }

    override val mViewModel: AuthSplashViewModel by viewModel()

    override fun getLayoutId() = R.layout.fragment_splash
    override fun getVariableId() = BR.viewModel

    override fun onSetupViewModel(viewModel: AuthSplashViewModel): Unit = with(viewModel) {
        getUser().apply {
            successResponse { firebaseUser ->
                Log.i(TAG, "Signed IN: $firebaseUser")
                getKoin().setProperty(Config.KEY_USER_ID, firebaseUser.uid)
                startActivity(Intent(requireContext(), MainActivity::class.java))
                requireActivity().finish()
            }
            errorResponse {
                findNavController().navigate(R.id.action_splashFragment_to_authSignInFragment)
            }
        }
    }
}