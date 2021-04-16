package com.akopyan757.linkit.view.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.navigation.fragment.findNavController
import com.akopyan757.base.view.BaseFragment
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.R
import com.akopyan757.linkit.common.utils.AndroidUtils
import com.akopyan757.linkit.databinding.FragmentAuthStartBinding
import com.akopyan757.linkit.viewmodel.AuthStartViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class AuthStartFragment: BaseFragment<FragmentAuthStartBinding, AuthStartViewModel>() {

    override val viewModel: AuthStartViewModel by viewModel()

    override fun getLayoutId(): Int = R.layout.fragment_auth_start
    override fun getVariableId(): Int = BR.viewModel

    override fun onSetupView(bundle: Bundle?) {
        with(binding) {
            tvAuthStartSignIn.setOnClickListener { openSignInScreen() }
            btnAuthStartSignUp.setOnClickListener { openSignUpScreen() }
            btnAuthService.setOnClickListener { this@AuthStartFragment.viewModel.signInService() }
        }
        with(viewModel) {
            getSignInIntentLive().observe(viewLifecycleOwner, { data ->
                startActivityForResult(data, SERVICE_SIGN_IN_REQUEST_CODE)
            })
            showMainScreenWithUserLive().observe(viewLifecycleOwner, { userUid ->
                openMainScreen(userUid)
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AuthSignInFragment.SERVICE_SIGN_IN_REQUEST_CODE) {
            hideActivityKeyboard()
            viewModel.getUserFromService(data)
        }
    }

    private fun hideActivityKeyboard() {
        AndroidUtils.hideKeyboard(requireActivity())
    }

    private fun openSignUpScreen() {
        findNavController().navigate(R.id.action_auth_start_to_sign_up)
    }

    private fun openSignInScreen() {
        findNavController().navigate(R.id.action_auth_start_to_sign_in)
    }

    companion object {
        const val SERVICE_SIGN_IN_REQUEST_CODE = 7676
    }
}