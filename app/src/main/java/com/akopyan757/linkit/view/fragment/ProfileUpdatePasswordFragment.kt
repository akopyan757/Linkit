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

    override val viewModel: ProfileUpdatePasswordViewModel by viewModel()

    override fun getLayoutId(): Int = R.layout.fragment_auth_update_password
    override fun getVariableId(): Int = BR.viewModel

    override fun onSetupView(bundle: Bundle?) {
        binding.ivUpdatePasswordBack.setOnClickListener { backToProfileDialog() }
        binding.btnUpdatePasswordSend.setOnClickListener { setPasswordToAccount() }

        with(viewModel) {
            getErrorResLive().observe(viewLifecycleOwner, { errorMessageRes ->
                viewModel.setErrorMessage(getString(errorMessageRes))
            })
            getThrowableLive().observe(viewLifecycleOwner, { throwable ->
                viewModel.setErrorMessage(throwable.localizedMessage ?: Config.ERROR)
                showToast(R.string.error)
            })
        }
    }

    private fun setPasswordToAccount() {
        hideActivityKeyboard()
        viewModel.updatePassword()
    }

    override fun onAction(action: Int) {
        if (action == ProfileUpdatePasswordViewModel.ACTION_SUCCESS_UPDATE) {
            showToast(R.string.password_updated)
            backToProfileDialog()
        }
    }

    private fun hideActivityKeyboard() {
        AndroidUtils.hideKeyboard(requireActivity())
    }

    private fun backToProfileDialog() {
        findNavController().popBackStack()
    }
}