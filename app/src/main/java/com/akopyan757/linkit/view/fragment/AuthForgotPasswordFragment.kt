package com.akopyan757.linkit.view.fragment

import android.os.Bundle
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.akopyan757.base.view.BaseFragment
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.R
import com.akopyan757.linkit.databinding.FragmentAuthResetPasswordBinding
import com.akopyan757.linkit.view.service.FirebaseEmailAuthorizationService
import com.akopyan757.linkit.viewmodel.AuthForgotPasswordViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.parameter.parametersOf

class AuthForgotPasswordFragment: BaseFragment<FragmentAuthResetPasswordBinding, AuthForgotPasswordViewModel>(), KoinComponent {

    override val mViewModel: AuthForgotPasswordViewModel by viewModel()

    private val mSignInService: FirebaseEmailAuthorizationService by inject { parametersOf(requireActivity()) }

    override fun getLayoutId(): Int = R.layout.fragment_auth_reset_password
    override fun getVariableId(): Int = BR.viewModel

    override fun onSetupView(binding: FragmentAuthResetPasswordBinding, bundle: Bundle?) = with(binding) {
        btnAuthForgotPassword.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    mSignInService.resetPassword(mViewModel.email)
                    val message = getString(R.string.toast_reset_password, mViewModel.email)
                    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                    findNavController().popBackStack()
                } catch (e: Exception) {
                    mViewModel.error = e.localizedMessage ?: "Error"
                }
            }
        }
    }

    companion object {
        const val TAG = "AUTH_SIGN_IN_FRAGMENT"
    }
}