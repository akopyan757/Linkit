package com.akopyan757.linkit.view.dialog

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import com.akopyan757.base.view.BaseDialogFragment
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.R
import com.akopyan757.linkit.databinding.DialogProfileBinding
import com.akopyan757.linkit.view.AuthActivity
import com.akopyan757.linkit.viewmodel.ProfileViewModel
import com.akopyan757.linkit.view.scope.mainInject
import com.akopyan757.linkit.view.service.FirebaseEmailAuthorizationService
import com.akopyan757.linkit.view.service.IAuthorizationService
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent
import org.koin.core.parameter.parametersOf

class ProfileDialogFragment : BaseDialogFragment<DialogProfileBinding, ProfileViewModel>(), KoinComponent {

    private val mAuthorizationService: IAuthorizationService by inject { parametersOf(requireActivity()) }
    private val mSignInService: FirebaseEmailAuthorizationService by inject { parametersOf(requireActivity()) }

    private val firebaseAuth: FirebaseAuth by mainInject()

    override val mViewModel: ProfileViewModel by viewModel()

    override fun getLayoutId(): Int = R.layout.dialog_profile
    override fun getVariableId(): Int = BR.viewModel

    override fun onSetupView(binding: DialogProfileBinding, bundle: Bundle?) = with(binding) {

        btnProfileSignOut.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                mAuthorizationService.signOut()
                mSignInService.sinOut()
                val activity = requireActivity()
                startActivity(Intent(activity, AuthActivity::class.java))
                activity.finish()
            }
        }
    }

    override fun onSetupViewModel(viewModel: ProfileViewModel, owner: LifecycleOwner): Unit = with(viewModel) {
        firebaseAuth.currentUser?.also { firebaseUser ->
            mViewModel.setUser(firebaseUser)
        }
    }
}