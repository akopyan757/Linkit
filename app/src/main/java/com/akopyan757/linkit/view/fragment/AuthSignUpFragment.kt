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
        with (binding) {
            btnAuthSignUp.setOnClickListener { signUpRequest() }
            ivAuthSignUpBack.setOnClickListener { backToStartScreen() }
        }
        with(viewModel) {
            getErrorResLive().observe(viewLifecycleOwner, { errorMessageRes ->
                viewModel.setErrorMessage(getString(errorMessageRes))
            })
            openMainScreenByUserUid().observe(viewLifecycleOwner, { userUid ->
                openMainScreen(userUid)
            })
            getThrowableLive().observe(viewLifecycleOwner, { throwable ->
                showErrorToast(throwable)
                if (throwable is FirebaseAuthUserCollisionException) {
                    showUserCollisionErrorDialog()
                }
            })
        }
    }

    private fun signUpRequest() {
        hideActivityKeyboard()
        viewModel.signUp()
    }

    private fun hideActivityKeyboard() {
        AndroidUtils.hideKeyboard(requireActivity())
    }

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

    private fun backToStartScreen() {
        findNavController().popBackStack()
    }
}