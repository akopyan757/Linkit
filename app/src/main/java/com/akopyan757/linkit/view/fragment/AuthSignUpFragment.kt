package com.akopyan757.linkit.view.fragment

import android.content.Intent
import android.os.Bundle
import com.akopyan757.base.model.ApiResponse
import com.akopyan757.base.view.BaseFragment
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.R
import com.akopyan757.linkit.databinding.FragmentAuthSignUpBinding
import com.akopyan757.linkit.view.MainActivity
import com.akopyan757.linkit.view.service.FirebaseEmailAuthorizationService
import com.akopyan757.linkit.viewmodel.AuthSignUpViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent
import org.koin.core.parameter.parametersOf

class AuthSignUpFragment: BaseFragment<FragmentAuthSignUpBinding, AuthSignUpViewModel>(), KoinComponent {

    override val mViewModel: AuthSignUpViewModel by viewModel()

    private val mSignInService: FirebaseEmailAuthorizationService by lazy {
        getKoin().get { parametersOf(requireActivity()) }
    }

    override fun getLayoutId(): Int = R.layout.fragment_auth_sign_up
    override fun getVariableId(): Int = BR.viewModel

    override fun onSetupView(binding: FragmentAuthSignUpBinding, bundle: Bundle?) = with(binding) {
        btnAuthSignUp.setOnClickListener {
            mViewModel.startRegistration()
        }
    }

    override fun onSetupViewModel(viewModel: AuthSignUpViewModel): Unit = with(viewModel) {
        initRes(getString(R.string.error_passwords_match))
    }

    override fun onAction(action: Int) {
        when (action) {
            AuthSignUpViewModel.ACTION_REGISTRATION -> {
                CoroutineScope(Dispatchers.Main).launch {
                    when (val response = mSignInService.createUser(mViewModel.email, mViewModel.password)) {
                        is ApiResponse.Success -> {
                            startActivity(Intent(requireContext(), MainActivity::class.java))
                            requireActivity().finish()
                        }

                        is ApiResponse.Error -> {
                            mViewModel.error = response.exception.localizedMessage ?: ""
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val TAG = "AUTH_SIGN_IN_FRAGMENT"
    }
}