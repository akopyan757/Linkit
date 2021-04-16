package com.akopyan757.linkit.view.fragment

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.akopyan757.base.view.BaseFragment
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.R
import com.akopyan757.linkit.common.utils.AndroidUtils
import com.akopyan757.linkit.databinding.FragmentAuthResetPasswordBinding
import com.akopyan757.linkit.viewmodel.AuthForgotPasswordViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent

class AuthForgotPasswordFragment: BaseFragment<FragmentAuthResetPasswordBinding, AuthForgotPasswordViewModel>(), KoinComponent {

    override val viewModel: AuthForgotPasswordViewModel by viewModel()

    override fun getLayoutId(): Int = R.layout.fragment_auth_reset_password
    override fun getVariableId(): Int = BR.viewModel

    override fun onSetupView(bundle: Bundle?) {
        binding.btnAuthForgotPassword.setOnClickListener { resetPasswordRequest() }

        viewModel.getThrowableLive().observe(viewLifecycleOwner, { throwable ->
            showErrorToast(throwable)
        })
    }

    private fun resetPasswordRequest() {
        hideActivityKeyboard()
        viewModel.resetPassword()
    }

    override fun onAction(action: Int) {
        if (action == AuthForgotPasswordViewModel.ACTION_RESET_SUCCESS) {
            showResetErrorToast()
            backToSignInScreen()
        }
    }

    private fun hideActivityKeyboard() {
        AndroidUtils.hideKeyboard(requireActivity())
    }

    private fun showResetErrorToast() {
        showToast(getString(R.string.toast_reset_password, viewModel.email))
    }

    private fun backToSignInScreen() {
        findNavController().popBackStack()
    }
}