package com.akopyan757.linkit.view.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.akopyan757.base.model.ApiResponse
import com.akopyan757.linkit.R
import com.akopyan757.linkit.view.MainActivity
import com.akopyan757.linkit.view.service.FirebaseEmailAuthorizationService
import com.akopyan757.linkit.view.service.IAuthorizationService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.parameter.parametersOf

class AuthSplashFragment: Fragment(), KoinComponent {

    companion object {
        private const val TAG = "AUTH_SPLASH_FRAGMENT"
    }

    private val mAuthorizationService: IAuthorizationService by lazy {
        getKoin().get { parametersOf(requireActivity()) }
    }

    private val mSignInService: FirebaseEmailAuthorizationService by lazy {
        getKoin().get { parametersOf(requireActivity()) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_splash, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        CoroutineScope(Dispatchers.Main).launch {
            Log.i(TAG, "FirebaseUser=${mSignInService.getUser()}")
            if (mSignInService.getUser() != null) {
                startActivity(Intent(requireContext(), MainActivity::class.java))
                requireActivity().finish()
            } else {
                findNavController().navigate(R.id.action_splashFragment_to_authSignInFragment)
            }

                 /*
              else {
                findNavController().navigate(R.id.action_splashFragment_to_authSignInFragment)
                when (mAuthorizationService.silentSignIn()) {
                    is ApiResponse.Success -> {
                        startActivity(Intent(requireContext(), MainActivity::class.java))
                        requireActivity().finish()
                    }

                    is ApiResponse.Error -> {
                        findNavController().navigate(R.id.action_splashFragment_to_authSignInFragment)
                    }

                    else -> {}
                }
            }*/
        }
    }
}