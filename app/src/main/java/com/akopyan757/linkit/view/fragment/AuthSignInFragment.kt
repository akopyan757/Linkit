package com.akopyan757.linkit.view.fragment

import android.content.Intent
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.akopyan757.base.view.BaseFragment
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.R
import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit.common.utils.AndroidUtils
import com.akopyan757.linkit.databinding.FragmentAuthSignInBinding
import com.akopyan757.linkit.view.MainActivity
import com.akopyan757.linkit.viewmodel.AuthSignInViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent

class AuthSignInFragment: BaseFragment<FragmentAuthSignInBinding, AuthSignInViewModel>(), KoinComponent {

    override val mViewModel: AuthSignInViewModel by viewModel()

    override fun getLayoutId(): Int = R.layout.fragment_auth_sign_in
    override fun getVariableId(): Int = BR.viewModel

    override fun onSetupView(
        binding: FragmentAuthSignInBinding,
        bundle: Bundle?
    ) = with(binding) {

        btnAuthHuawei.setOnClickListener {
            startActivityForResult(mViewModel.getSignInIntent(), SERVICE_SIGN_IN_REQUEST_CODE)
        }

        btnAuthSignIn.setOnClickListener {
            signInWithEmailAndPasswordRequest()
        }

        tvAuthForgotPasswordButton.setOnClickListener {
            findNavController().navigate(R.id.action_authSignInFragment_to_authForgotPasswordFragment)
        }

        tvAuthRegistrationButton.setOnClickListener {
            findNavController().navigate(R.id.action_authSignInFragment_to_authSignUpFragment)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode.isServiceSignIn())
            signInWithServiceRequest(data)
    }

    private fun signInWithEmailAndPasswordRequest() {
        mViewModel.getSignInWithEmailLiveResponse().apply {
            observeLoadingResponse { AndroidUtils.hideKeyboard(requireActivity()) }
            observeSuccessResponse { firebaseUid -> openMainScreen(firebaseUid) }
            observeErrorResponse { exception -> showErrorToast(exception) }
        }
    }

    private fun signInWithServiceRequest(data: Intent?) {
        mViewModel.getSignInWithServiceLiveResponse(data).apply {
            observeLoadingResponse { AndroidUtils.hideKeyboard(requireActivity()) }
            observeSuccessResponse { firebaseUid -> openMainScreen(firebaseUid) }
            observeErrorResponse { exception -> showErrorToast(exception) }
        }
    }

    private fun Int.isServiceSignIn() = this == SERVICE_SIGN_IN_REQUEST_CODE

    companion object {
        const val SERVICE_SIGN_IN_REQUEST_CODE = 7676
        const val TAG = "AUTH_SIGN_IN_FRAGMENT"
    }
}