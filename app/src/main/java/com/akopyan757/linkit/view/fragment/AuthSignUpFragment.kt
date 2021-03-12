package com.akopyan757.linkit.view.fragment

import android.app.AlertDialog
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.akopyan757.base.view.BaseFragment
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.R
import com.akopyan757.linkit.common.utils.AndroidUtils
import com.akopyan757.linkit.databinding.FragmentAuthSignUpBinding
import com.akopyan757.linkit.viewmodel.AuthSignUpViewModel
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent

class AuthSignUpFragment: BaseFragment<FragmentAuthSignUpBinding, AuthSignUpViewModel>(), KoinComponent {

    override val viewModel: AuthSignUpViewModel by viewModel()

    override fun getLayoutId(): Int = R.layout.fragment_auth_sign_up
    override fun getVariableId(): Int = BR.viewModel

    override fun onSetupView(bundle: Bundle?) {
        binding.tvAuthSignInBack.setOnClickListener { backToSignInScreen() }
        binding.btnAuthSignUp.setOnClickListener { signUpRequest() }
    }

    private fun backToSignInScreen() {
        findNavController().popBackStack()
    }

    private fun signUpRequest() {
        viewModel.requestSignUp().apply {
            observeEmptyResponse { viewModel.setErrorMessage(getString(R.string.error_passwords_match)) }
            observeLoadingResponse { AndroidUtils.hideKeyboard(requireActivity()) }
            observeSuccessResponse { uid -> openMainScreen(uid) }
            observeErrorResponse { exception ->
                if (exception.isUserCollision())
                    showUserCollisionErrorDialog()
                showErrorToast(exception)
            }
        }
    }

    private fun Exception.isUserCollision() = this is FirebaseAuthUserCollisionException

    private fun showUserCollisionErrorDialog() {
        val method = getString(R.string.signInMethod)
        val message = getString(R.string.errorMessageAuthUserCollision, method)
        AlertDialog.Builder(context, R.style.Theme_Linkit_AlertDialog)
            .setTitle(R.string.error)
            .setMessage(message)
            .setPositiveButton(R.string.ok) { dialogInterface, _ ->
                dialogInterface?.dismiss()
            }.create()
            .show()
    }

    companion object {
        const val TAG = "AUTH_SIGN_IN_FRAGMENT"
    }
}