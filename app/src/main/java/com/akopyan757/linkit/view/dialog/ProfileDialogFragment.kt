package com.akopyan757.linkit.view.dialog

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
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
        btnProfileSetupPassword.setOnClickListener { openSetPasswordScreen() }
        btnProfileChangePassword.setOnClickListener { openUpdatePasswordScreen() }
        btnProfileVerify.setOnClickListener { requestVerifyEmail() }
        btnProfileSignOut.setOnClickListener { requestSignOut() }
        requestGetUserData()
        observeEmailVerifyLiveState()
    }

    private fun requestGetUserData() {
        mViewModel.getUserResponseLive().apply {
            observeSuccessResponse {}
            observeErrorResponse{ dismiss() }
        }
    }

    private fun requestVerifyEmail() {
        mViewModel.getVerifyLiveResponse().observeSuccessResponse { email ->
            showToast(getString(R.string.toast_verify_email, email))
        }
    }

    private fun requestSignOut() {
        mViewModel.getSignOutResponseLive().observeSuccessResponse {
            showAuthorizationScreen()
        }
    }

    private fun observeEmailVerifyLiveState() {
        mViewModel.getEmailVerifyState().observe(viewLifecycleOwner) { isEmailVerify ->
            if (isEmailVerify) {
                resetVisibleVerifyButton()
                setVisibleVerifyIcon()
            } else {
                setVisibleVerifyButton()
                resetVisibleVerifyIcon()
            }
        }
    }

    private fun showAuthorizationScreen() {
        startActivity(Intent(requireActivity(), AuthActivity::class.java))
        requireActivity().finish()
    }

    private fun openSetPasswordScreen() {
        findNavController().navigate(R.id.action_profileDialogFragment_to_setPassword)
    }

    private fun openUpdatePasswordScreen() {
        findNavController().navigate(R.id.action_profileDialogFragment_to_updatePassword)
    }

    private fun setVisibleVerifyIcon() {
        val iconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_check_verify)
        val nullDrawable = null
        mBinding.tvProfileEmail.setCompoundDrawablesWithIntrinsicBounds(
            nullDrawable, nullDrawable, iconDrawable, nullDrawable
        )
    }

    private fun resetVisibleVerifyIcon() {
        val nullDrawable = null
        mBinding.tvProfileEmail.setCompoundDrawablesRelative(
            nullDrawable, nullDrawable, nullDrawable, nullDrawable
        )
    }

    private fun setVisibleVerifyButton() {
        mBinding.btnProfileVerify.visibility = View.VISIBLE
    }

    private fun resetVisibleVerifyButton() {
        mBinding.btnProfileVerify.visibility = View.GONE
    }
}