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

    override val mViewModel: AuthForgotPasswordViewModel by viewModel()

    override fun getLayoutId(): Int = R.layout.fragment_auth_reset_password
    override fun getVariableId(): Int = BR.viewModel

    override fun onSetupView(bundle: Bundle?) {
        mBinding.btnAuthForgotPassword.setOnClickListener { resetPasswordRequest() }
    }

    private fun resetPasswordRequest() {
        mViewModel.requestResetPassword().apply {
            observeLoadingResponse { AndroidUtils.hideKeyboard(requireActivity()) }
            observeSuccessResponse { email ->
                showToast(getString(R.string.toast_reset_password, email))
                backToSignInScreen()
            }
            observeErrorResponse { exception -> showErrorToast(exception) }
        }
    }

    private fun backToSignInScreen() {
        findNavController().popBackStack()
    }
}