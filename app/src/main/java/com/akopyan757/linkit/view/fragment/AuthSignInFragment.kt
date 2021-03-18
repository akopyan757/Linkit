package com.akopyan757.linkit.view.fragment

import android.content.Intent
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
        binding.btnAuthHuawei.setOnClickListener { signInWithSpecificService() }
        binding.btnAuthSignIn.setOnClickListener { signInWithEmailAndPassword() }
        binding.tvAuthForgotPassword.setOnClickListener { openForgotPasswordScreen() }
        binding.tvAuthRegistration.setOnClickListener { openSignUpScreen() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == SERVICE_SIGN_IN_REQUEST_CODE) {
            handleDataFromSpecificService(data)
        }
    }

    private fun signInWithSpecificService() {
        startActivityForResult(viewModel.getSignInIntent(), SERVICE_SIGN_IN_REQUEST_CODE)
    }

    private fun signInWithEmailAndPassword() {
        viewModel.requestSignInWithEmail().apply {
            observeLoadingResponse { AndroidUtils.hideKeyboard(requireActivity()) }
            observeSuccessResponse { firebaseUid -> openMainScreen(firebaseUid) }
            observeErrorResponse { exception -> showErrorToast(exception) }
        }
    }

    private fun handleDataFromSpecificService(data: Intent?) {
        viewModel.requestSignInWithService(data).apply {
            observeLoadingResponse { AndroidUtils.hideKeyboard(requireActivity()) }
            observeSuccessResponse { firebaseUid -> openMainScreen(firebaseUid) }
            observeErrorResponse { exception -> showErrorToast(exception) }
        }
    }

    private fun openForgotPasswordScreen() {
        findNavController().navigate(R.id.action_authSignInFragment_to_authForgotPasswordFragment)
    }

    private fun openSignUpScreen() {
        findNavController().navigate(R.id.action_authSignInFragment_to_authSignUpFragment)
    }

    companion object {
        const val SERVICE_SIGN_IN_REQUEST_CODE = 7676
    }
}