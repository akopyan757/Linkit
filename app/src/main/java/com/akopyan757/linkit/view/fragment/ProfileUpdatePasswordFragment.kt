package com.akopyan757.linkit.view.fragment

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.akopyan757.base.view.BaseFragment
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.R
import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit.common.utils.AndroidUtils
import com.akopyan757.linkit.databinding.FragmentAuthUpdatePasswordBinding
import com.akopyan757.linkit.viewmodel.ProfileUpdatePasswordViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent

class ProfileUpdatePasswordFragment: BaseFragment<FragmentAuthUpdatePasswordBinding, ProfileUpdatePasswordViewModel>(), KoinComponent {

    override val mViewModel: ProfileUpdatePasswordViewModel by viewModel()

    override fun getLayoutId(): Int = R.layout.fragment_auth_update_password
    override fun getVariableId(): Int = BR.viewModel

    override fun onSetupView(bundle: Bundle?) {
        mBinding.ivUpdatePasswordBack.setOnClickListener { findNavController().popBackStack() }
        mBinding.btnUpdatePasswordSend.setOnClickListener { setPasswordToAccount() }
    }

    private fun setPasswordToAccount() {
        mViewModel.requestSetPassword().apply {
            observeEmptyResponse {
                mViewModel.setErrorMessage(getString(R.string.error_passwords_match))
            }
            observeLoadingResponse { AndroidUtils.hideKeyboard(requireActivity()) }
            observeSuccessResponse {
                showToast(R.string.password_updated)
                backToProfileDialog()
            }
            observeErrorResponse { exception ->
                mViewModel.setErrorMessage(exception.localizedMessage ?: Config.ERROR)
                showToast(R.string.error)
            }
        }
    }

    private fun backToProfileDialog() {
        findNavController().popBackStack()
    }
}