package com.akopyan757.linkit.view.fragment

import android.content.Intent
import androidx.databinding.ViewDataBinding
import com.akopyan757.base.view.BaseFragment
import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit.view.MainActivity
import org.koin.android.ext.android.getKoin

fun <V: ViewDataBinding, T: BaseViewModel> BaseFragment<V, T>.openMainScreen(userId: String) {
    getKoin().setProperty(Config.KEY_USER_ID, userId)
    startActivity(Intent(requireContext(), MainActivity::class.java))
    requireActivity().finish()
}