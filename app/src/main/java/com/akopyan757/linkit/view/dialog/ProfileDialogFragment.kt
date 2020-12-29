package com.akopyan757.linkit.view.dialog

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
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

    override fun onSetupView(binding: DialogProfileBinding, bundle: Bundle?) = with(binding) {
        btnProfileSetupPassword.setOnClickListener {
            findNavController().navigate(R.id.action_profileDialogFragment_to_setPassword)
        }
    }

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
        getVerifyResponseLive().apply {
            successResponse { email ->
                val message = getString(R.string.toast_verify_email, email)
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
            }
        }
        getEmailVerifyState().observe(viewLifecycleOwner, { isEmailVerify ->
            val nullDrawable = null
            if (isEmailVerify) {
                val iconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_check_verify)
                mBinding.btnProfileVerify.visibility = View.GONE
                mBinding.tvProfileEmail.setCompoundDrawablesWithIntrinsicBounds(
                    nullDrawable, nullDrawable, iconDrawable, nullDrawable
                )
            } else {
                mBinding.btnProfileVerify.visibility = View.VISIBLE
                mBinding.tvProfileEmail.setCompoundDrawablesRelative(
                    nullDrawable, nullDrawable, nullDrawable, nullDrawable
                )
            }
        })
    }
}