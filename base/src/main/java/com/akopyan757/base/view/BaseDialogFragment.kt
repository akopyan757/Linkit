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
import androidx.annotation.StyleRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LiveData
import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.base.viewmodel.DiffItemObservable
import com.akopyan757.base.viewmodel.list.ListHolder
import com.akopyan757.base.viewmodel.list.UpdatableListAdapter
import com.akopyan757.base.viewmodel.list.observeList
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseDialogFragment<V: ViewDataBinding, T: BaseViewModel> : BottomSheetDialogFragment() {

    protected abstract val viewModel: T
    protected lateinit var binding: V

    protected open fun onAction(action: Int) {}
    protected open fun onSetupView(bundle: Bundle?) {}
    abstract fun getLayoutId(): Int
    abstract fun getVariableId(): Int
    @StyleRes open fun getStyleId(): Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val styleId = getStyleId()
        if (styleId != null) {
            setStyle(STYLE_NORMAL, styleId)
            setTransparentDialogBackground()
        }
    }

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

    fun <T : DiffItemObservable> LiveData<ListHolder<T>>.observeList(
        adapter: UpdatableListAdapter<T>,
        afterError: (() -> Unit)? = null
    ) = this.observeList(viewLifecycleOwner, adapter, afterError)
}