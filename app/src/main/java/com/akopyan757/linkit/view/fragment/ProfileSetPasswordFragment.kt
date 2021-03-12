package com.akopyan757.linkit.view.fragment

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.akopyan757.base.view.BaseFragment
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.R
import com.akopyan757.linkit.common.utils.AndroidUtils
import com.akopyan757.linkit.databinding.FragmentAuthSetPasswordBinding
import com.akopyan757.linkit.viewmodel.ProfileSetPasswordViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent

class ProfileSetPasswordFragment: BaseFragment<FragmentAuthSetPasswordBinding, ProfileSetPasswordViewModel>(), KoinComponent {

    override val mViewModel: ProfileSetPasswordViewModel by viewModel()

    override fun getLayoutId(): Int = R.layout.fragment_auth_set_password
    override fun getVariableId(): Int = BR.viewModel

    override fun onSetupView(binding: FragmentAuthSetPasswordBinding, bundle: Bundle?) = with(binding) {
        ivSetPasswordBack.setOnClickListener {
            findNavController().popBackStack()
        }
        btnSetPasswordSend.setOnClickListener { setPasswordRequest() }
    }

    private fun setPasswordRequest() {
        mViewModel.getSetPasswordResponseLive().apply {
            observeEmptyResponse {
                mViewModel.setErrorMessage(getString(R.string.error_passwords_match))
            }
            observeLoadingResponse { AndroidUtils.hideKeyboard(requireActivity()) }
            observeSuccessResponse {
                showToast(R.string.password_set)
                findNavController().popBackStack()
            }
            observeErrorResponse { showToast(R.string.error) }
        }
    }
}