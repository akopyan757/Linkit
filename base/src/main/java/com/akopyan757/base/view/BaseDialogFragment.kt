package com.akopyan757.base.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LiveData
import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.base.viewmodel.DiffItemObservable
import com.akopyan757.base.viewmodel.list.ListHolder
import com.akopyan757.base.viewmodel.list.UpdatableListAdapter
import com.akopyan757.base.viewmodel.list.observeList

abstract class BaseDialogFragment<V: ViewDataBinding, T: BaseViewModel> : DialogFragment() {

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
        setTransparentDialogBackground()
        observeActions()
        onSetupView(arguments)
        return binding.root
    }

    private fun setTransparentDialogBackground() {
        val window = dialog?.window ?: return
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window.requestFeature(Window.FEATURE_NO_TITLE)
    }

    private fun observeActions() {
        viewModel.getLiveAction().observe(viewLifecycleOwner, { action -> onAction(action) })
    }

    fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    fun showToast(@StringRes messageRes: Int) {
        Toast.makeText(requireContext(), resources.getText(messageRes), Toast.LENGTH_LONG).show()
    }

    fun showErrorToast(exception: Exception) {
        val message = exception.localizedMessage ?: "Error"
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    fun <T> LiveData<out BaseViewModel.ResponseState<T>>.observeErrorResponse(
            onAction: (Exception) -> Unit
    ) = errorResponse(viewLifecycleOwner, onAction)

    fun <T> LiveData<out BaseViewModel.ResponseState<T>>.observeEmptyResponse(
            onAction: (T) -> Unit
    ) = successResponse(viewLifecycleOwner, onAction)

    fun <T> LiveData<out BaseViewModel.ResponseState<T>>.observeSuccessResponse(
            onAction: (T) -> Unit
    ) = successResponse(viewLifecycleOwner, onAction)

    fun LiveData<out BaseViewModel.ResponseState<Unit>>.observeSuccessResponse(
            onAction: () -> Unit
    ) = successEmptyResponse(viewLifecycleOwner, onAction)

    fun <T> LiveData<out BaseViewModel.ResponseState<T>>.observeLoadingResponse(
            onAction: () -> Unit
    ) = loadingResponse(viewLifecycleOwner, onAction)

    fun <T : DiffItemObservable> LiveData<ListHolder<T>>.observeList(
        adapter: UpdatableListAdapter<T>,
        afterError: (() -> Unit)? = null
    ) = this.observeList(viewLifecycleOwner, adapter, afterError)
}