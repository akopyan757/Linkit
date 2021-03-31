package com.akopyan757.linkit.view.fragment

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.akopyan757.base.view.BaseFragment
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.R
import com.akopyan757.linkit.common.utils.AndroidUtils
import com.akopyan757.linkit.databinding.FragmentAuthSignInBinding
import com.akopyan757.linkit.viewmodel.AuthSignInViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent

class AuthSignInFragment: BaseFragment<FragmentAuthSignInBinding, AuthSignInViewModel>(), KoinComponent {

    override val viewModel: AuthSignInViewModel by viewModel()

    override fun getLayoutId(): Int = R.layout.fragment_auth_sign_in
    override fun getVariableId(): Int = BR.viewModel

    override fun onSetupView(bundle: Bundle?) {
        with(binding) {
            btnAuthSignIn.setOnClickListener { signInWithEmailAndPassword() }
            tvAuthForgotPassword.setOnClickListener { openForgotPasswordScreen() }
            ivAuthSignInBack.setOnClickListener { backToStartScreen() }
        }
    }

    private fun signInWithEmailAndPassword() {
        viewModel.requestSignInWithEmail().apply {
            observeLoadingResponse { AndroidUtils.hideKeyboard(requireActivity()) }
            observeSuccessResponse { firebaseUid -> openMainScreen(firebaseUid) }
            observeErrorResponse { exception -> showErrorToast(exception) }
        }
    }

    private fun openForgotPasswordScreen() {
        findNavController().navigate(R.id.action_auth_sign_in_to_forgot_password)
    }

    private fun backToStartScreen() {
        findNavController().popBackStack()
    }

    companion object {
        const val SERVICE_SIGN_IN_REQUEST_CODE = 7676
    }
}