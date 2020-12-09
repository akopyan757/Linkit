package com.akopyan757.linkit.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.akopyan757.linkit.R
import com.akopyan757.linkit.databinding.ActivityRecieveUrlBinding
import com.akopyan757.linkit.viewmodel.LinkCreateUrlViewModel
import com.google.android.material.snackbar.Snackbar
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent

class ReceiveUrlActivity : AppCompatActivity(), KoinComponent {

    private val mViewModel: LinkCreateUrlViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityRecieveUrlBinding>(
            this, R.layout.activity_recieve_url
        ).apply {
            viewModel = mViewModel
        }

        setSupportActionBar(findViewById(R.id.toolbar))

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }
}