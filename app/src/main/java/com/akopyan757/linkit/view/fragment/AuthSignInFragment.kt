package com.akopyan757.linkit.view.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.navigation.fragment.findNavController
import com.akopyan757.base.model.ApiResponse
import com.akopyan757.base.view.BaseFragment
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.R
import com.akopyan757.linkit.databinding.FragmentAuthSignInBinding
import com.akopyan757.linkit.view.MainActivity
import com.akopyan757.linkit.view.service.FirebaseEmailAuthorizationService
import com.akopyan757.linkit.view.service.IAuthorizationService
import com.akopyan757.linkit.viewmodel.AuthSignInViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent
import org.koin.core.parameter.parametersOf

class AuthSignInFragment: BaseFragment<FragmentAuthSignInBinding, AuthSignInViewModel>(), KoinComponent {

    override val mViewModel: AuthSignInViewModel by viewModel()

    private val mAuthorizationService: IAuthorizationService by lazy {
        getKoin().get { parametersOf(requireActivity()) }
    }

    private val mSignInService: FirebaseEmailAuthorizationService by lazy {
        getKoin().get { parametersOf(requireActivity()) }
    }

    override fun getLayoutId(): Int = R.layout.fragment_auth_sign_in
    override fun getVariableId(): Int = BR.viewModel

    override fun onSetupView(
        binding: FragmentAuthSignInBinding,
        bundle: Bundle?
    ) = with(binding) {
        btnAuthHuawei.setOnClickListener {
            val intent = mAuthorizationService.getSignInIntent()
            requireActivity().startActivityForResult(intent, REQUEST_CODE)
        }

        btnAuthSignIn.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                when (val response = mSignInService.signIn(mViewModel.email, mViewModel.password)) {
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


        tvAuthRegistrationButton.setOnClickListener {
            findNavController().navigate(R.id.action_authSignInFragment_to_authSignUpFragment)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.i(TAG, "onActivityResult($requestCode, $resultCode)")
        if (requestCode == REQUEST_CODE) {

            CoroutineScope(Dispatchers.IO).launch {

                val firebaseUser = mAuthorizationService.getUserAfterAuthorization(data)
                Log.i(TAG, "onActivityResult: $firebaseUser")

                CoroutineScope(Dispatchers.Main).launch {
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                    requireActivity().finish()
                }
            }

        }
    }

    companion object {
        const val REQUEST_CODE = 7676
        const val TAG = "AUTH_SIGN_IN_FRAGMENT"
    }
}