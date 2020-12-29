package com.akopyan757.linkit.view.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.akopyan757.base.view.BaseFragment
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.R
import com.akopyan757.linkit.common.Config
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
            startActivityForResult(mViewModel.getSignInIntent(), REQUEST_CODE)
        }

        tvAuthForgotPasswordButton.setOnClickListener {
            findNavController().navigate(R.id.action_authSignInFragment_to_authForgotPasswordFragment)
        }

        tvAuthRegistrationButton.setOnClickListener {
            findNavController().navigate(R.id.action_authSignInFragment_to_authSignUpFragment)
        }
    }

    override fun onSetupViewModel(viewModel: AuthSignInViewModel): Unit = with(viewModel) {
        getSignInResponseLive().apply {
            successResponse { uid ->
                getKoin().setProperty(Config.KEY_USER_ID, uid)
                startActivity(Intent(requireContext(), MainActivity::class.java))
                requireActivity().finish()
            }
            errorResponse {
                Toast.makeText(requireContext(), "Error", Toast.LENGTH_LONG).show()
            }
        }
        getSignInServiceResponseLive().apply {
            successResponse { uid ->
                getKoin().setProperty(Config.KEY_USER_ID, uid)
                startActivity(Intent(requireContext(), MainActivity::class.java))
                requireActivity().finish()
            }
            errorResponse {
                Toast.makeText(requireContext(), "Error", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.i(TAG, "onActivityResult($requestCode, $resultCode)")
        if (requestCode == REQUEST_CODE) {
            mViewModel.onHuaweiDataResult(data)
        }
    }

    companion object {
        const val REQUEST_CODE = 7676
        const val TAG = "AUTH_SIGN_IN_FRAGMENT"
    }
}