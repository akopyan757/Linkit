package com.akopyan757.linkit.view.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.akopyan757.linkit.R
import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit.view.MainActivity
import com.akopyan757.linkit.view.service.FirebaseEmailAuthorizationService
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.parameter.parametersOf

class AuthSplashFragment: Fragment(), KoinComponent {

    companion object {
        private const val TAG = "AUTH_SPLASH_FRAGMENT"
    }

    private val mSignInService: FirebaseEmailAuthorizationService by inject { parametersOf(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_splash, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val firebaseUser = mSignInService.getUser()
        if (firebaseUser != null) {
            Log.i(TAG, "Firebase User exists: $firebaseUser")
            getKoin().setProperty(Config.KEY_USER_ID, firebaseUser.uid)
            startActivity(Intent(requireContext(), MainActivity::class.java))
            requireActivity().finish()
        } else {
            findNavController().navigate(R.id.action_splashFragment_to_authSignInFragment)
        }
    }
}