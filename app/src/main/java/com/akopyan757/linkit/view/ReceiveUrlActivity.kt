package com.akopyan757.linkit.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.akopyan757.linkit.R
import com.akopyan757.linkit.databinding.ActivityRecieveUrlBinding
import com.akopyan757.linkit.viewmodel.LinkCreateUrlViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent
import org.koin.core.parameter.parametersOf

class ReceiveUrlActivity : AppCompatActivity(), KoinComponent {

    companion object {
        private const val TAG = "RECEIVE_URL_ACTIVITY"
    }

    private val mViewModel: LinkCreateUrlViewModel by viewModel(
        parameters = { parametersOf(intent.getStringExtra(Intent.EXTRA_TEXT)) }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityRecieveUrlBinding>(
            this, R.layout.activity_recieve_url
        ).apply {
            viewModel = mViewModel
        }

        mViewModel.getLiveResponses().observe(this, {
            Log.i(TAG, "OBSERVER")
        })

        mViewModel.getLiveAction().observe(this, { actionId ->
            when (actionId) {
                LinkCreateUrlViewModel.ACTION_DISMISS -> {
                    finish()
                }
            }
        })

        setSupportActionBar(binding.toolbar)
    }
}