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

    override val viewModel: ProfileSetPasswordViewModel by viewModel()

    override fun getLayoutId(): Int = R.layout.fragment_auth_set_password
    override fun getVariableId(): Int = BR.viewModel

    override fun onSetupView(bundle: Bundle?) {
        binding.ivSetPasswordBack.setOnClickListener { backToMainScreen() }
        binding.btnSetPasswordSend.setOnClickListener { setPasswordRequest() }

        with(viewModel) {
            getErrorResLive().observe(viewLifecycleOwner, { errorMessageRes ->
                viewModel.setErrorMessage(getString(errorMessageRes))
            })
        }
    }

    private fun setPasswordRequest() {
        hideActivityKeyboard()
        viewModel.setPassword()
    }

    override fun onAction(action: Int) {
        if (action == ProfileSetPasswordViewModel.ACTION_LINK_SUCCESS) {
            showToast(R.string.password_set)
            backToMainScreen()
        }
    }

    private fun hideActivityKeyboard() {
        AndroidUtils.hideKeyboard(requireActivity())
    }

    private fun backToMainScreen() {
        findNavController().popBackStack()
    }
}