package com.akopyan757.linkit.view.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.akopyan757.base.view.BaseFragment
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.R
import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit.common.utils.AndroidUtils
import com.akopyan757.linkit.databinding.FragmentAuthSignUpBinding
import com.akopyan757.linkit.view.MainActivity
import com.akopyan757.linkit.viewmodel.AuthSignUpViewModel
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent

class AuthSignUpFragment: BaseFragment<FragmentAuthSignUpBinding, AuthSignUpViewModel>(), KoinComponent {

    override val mViewModel: AuthSignUpViewModel by viewModel()

    override fun getLayoutId(): Int = R.layout.fragment_auth_sign_up
    override fun getVariableId(): Int = BR.viewModel

    override fun onSetupView(binding: FragmentAuthSignUpBinding, bundle: Bundle?) = with(binding) {
        tvAuthSignInBackButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onSetupViewModel(viewModel: AuthSignUpViewModel): Unit = with(viewModel) {
        initRes(getString(R.string.error_passwords_match))
        getSignUpResponseLive().apply {
            loadingResponse {
                AndroidUtils.hideKeyboard(requireActivity())
            }
            successResponse { uid ->
                getKoin().setProperty(Config.KEY_USER_ID, uid)
                startActivity(Intent(requireContext(), MainActivity::class.java))
                requireActivity().finish()
            }
            errorResponse { exception ->
                when (exception) {
                    is FirebaseAuthUserCollisionException -> {
                        val method = getString(R.string.signInMethod)
                        val message = getString(R.string.errorMessageAuthUserCollision, method)
                        AlertDialog.Builder(context, R.style.Theme_Linkit_AlertDialog)
                            .setTitle(R.string.error)
                            .setMessage(message)
                            .setPositiveButton(R.string.ok) { dialogInterface, _ ->
                                dialogInterface?.dismiss()
                            }
                            .create()
                            .show()
                    }
                }

                Toast.makeText(requireContext(), R.string.error, Toast.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        const val TAG = "AUTH_SIGN_IN_FRAGMENT"
    }
}