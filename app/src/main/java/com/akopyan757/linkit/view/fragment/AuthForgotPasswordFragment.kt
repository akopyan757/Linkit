package com.akopyan757.linkit.view.fragment

import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.akopyan757.base.view.BaseFragment
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.R
import com.akopyan757.linkit.databinding.FragmentAuthResetPasswordBinding
import com.akopyan757.linkit.viewmodel.AuthForgotPasswordViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent

class AuthForgotPasswordFragment: BaseFragment<FragmentAuthResetPasswordBinding, AuthForgotPasswordViewModel>(), KoinComponent {

    override val mViewModel: AuthForgotPasswordViewModel by viewModel()

    override fun getLayoutId(): Int = R.layout.fragment_auth_reset_password
    override fun getVariableId(): Int = BR.viewModel

    override fun onSetupViewModel(viewModel: AuthForgotPasswordViewModel): Unit = with(viewModel) {
        getResetPasswordResponseLive().apply {
            successResponse { email ->
                val message = getString(R.string.toast_reset_password, email)
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                findNavController().popBackStack()
            }
            errorResponse {
                Toast.makeText(requireContext(), "Error", Toast.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        const val TAG = "AUTH_SIGN_IN_FRAGMENT"
    }
}