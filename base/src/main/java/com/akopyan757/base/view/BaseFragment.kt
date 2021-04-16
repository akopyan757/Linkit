package com.akopyan757.base.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.base.viewmodel.DiffItemObservable
import com.akopyan757.base.viewmodel.list.ListHolder
import com.akopyan757.base.viewmodel.list.UpdatableListAdapter
import com.akopyan757.base.viewmodel.list.observeList

abstract class BaseFragment<V: ViewDataBinding, T: BaseViewModel> : Fragment() {

    protected abstract val viewModel: T
    protected lateinit var binding: V

    protected open fun onAction(action: Int) {}
    protected open fun onSetupView(bundle: Bundle?) {}
    abstract fun getLayoutId(): Int
    abstract fun getVariableId(): Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        binding.setVariable(getVariableId(), viewModel)
        observeActions()
        onSetupView(arguments)
        return binding.root
    }

    private fun observeActions() {
        viewModel.getLiveAction().observe(viewLifecycleOwner, { action -> onAction(action) })
    }

    fun showToast(@StringRes messageRes: Int) {
        Toast.makeText(requireContext(), resources.getText(messageRes), Toast.LENGTH_LONG).show()
    }

    fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    fun showErrorToast(throwable: Throwable) {
        val message = throwable.localizedMessage ?: DEFAULT_ERROR_MESSAGE
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    fun showDialog(dialogFragment: DialogFragment, tag: String) {
        val activity = activity ?: return
        dialogFragment.show(activity.supportFragmentManager, tag)
    }

    fun <T : DiffItemObservable> LiveData<ListHolder<T>>.observeList(
        adapter: UpdatableListAdapter<T>,
        afterError: (() -> Unit)? = null
    ) = this.observeList(viewLifecycleOwner, adapter, afterError)

    companion object {
        private const val DEFAULT_ERROR_MESSAGE = "Error"
    }
}