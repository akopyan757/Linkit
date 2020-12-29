package com.akopyan757.base.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
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

    protected abstract val mViewModel: T

    protected lateinit var mBinding: V

    protected open fun onPropertyChanged(propertyId: Int) {}
    protected open fun onAction(action: Int) {}
    protected open fun onSetupView(binding: V, bundle: Bundle?) {}
    protected open fun onSetupViewModel(viewModel: T) {}

    abstract fun getLayoutId(): Int
    abstract fun getVariableId(): Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        mBinding.setVariable(getVariableId(), mViewModel)

        mViewModel.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                onPropertyChanged(propertyId)
            }
        })

        mViewModel.getLiveAction().observe(viewLifecycleOwner, { action ->
            onAction(action)
        })

        mViewModel.getLiveResponses()?.observe(viewLifecycleOwner, {
            Log.i("STATE", "OBSERVES")
        })

        onSetupView(mBinding, arguments)
        onSetupViewModel(mViewModel)

        return mBinding.root
    }

    fun showToast(@StringRes messageRes: Int) {
        val context = context ?: return
        Toast.makeText(context, resources.getText(messageRes), Toast.LENGTH_LONG).show()
    }

    fun showToast(message: String) {
        val context = context ?: return
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun showDialog(dialogFragment: DialogFragment, tag: String) {
        val activity = activity ?: return
        dialogFragment.show(activity.supportFragmentManager, tag)
    }

    fun <T> LiveData<out BaseViewModel.ResponseState<T>>.errorResponse(
        onAction: (Exception) -> Unit
    ) = errorResponse(viewLifecycleOwner, onAction)

    fun <T> LiveData<out BaseViewModel.ResponseState<T>>.successResponse(
        onAction: (T) -> Unit
    ) = successResponse(viewLifecycleOwner, onAction)

    fun <T : DiffItemObservable> LiveData<ListHolder<T>>.observeList(
        adapter: UpdatableListAdapter<T>,
        afterError: (() -> Unit)? = null
    ) = this.observeList(viewLifecycleOwner, adapter, afterError)
}