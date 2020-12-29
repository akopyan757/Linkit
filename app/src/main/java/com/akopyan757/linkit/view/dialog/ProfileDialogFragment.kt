package com.akopyan757.linkit.view.dialog

import android.content.Intent
import androidx.lifecycle.LifecycleOwner
import com.akopyan757.base.view.BaseDialogFragment
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.R
import com.akopyan757.linkit.databinding.DialogProfileBinding
import com.akopyan757.linkit.view.AuthActivity
import com.akopyan757.linkit.viewmodel.ProfileViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent

class ProfileDialogFragment : BaseDialogFragment<DialogProfileBinding, ProfileViewModel>(), KoinComponent {

    override val mViewModel: ProfileViewModel by viewModel()

    override fun getLayoutId(): Int = R.layout.dialog_profile
    override fun getVariableId(): Int = BR.viewModel

    override fun onSetupViewModel(viewModel: ProfileViewModel, owner: LifecycleOwner): Unit = with(viewModel) {
        getUserResponseLive().apply {
            successResponse {}
            errorResponse {}
        }
        getSignOutResponseLive().apply {
            successResponse {
                val activity = requireActivity()
                startActivity(Intent(activity, AuthActivity::class.java))
                activity.finish()
            }
        }
    }

    companion object {
        private const val TAG = "PROFILE_DIALOG_FRAGMENT"
    }
}