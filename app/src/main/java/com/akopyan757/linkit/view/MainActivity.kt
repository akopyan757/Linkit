package com.akopyan757.linkit.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.akopyan757.linkit.R
import org.koin.core.KoinComponent

class MainActivity : AppCompatActivity(), KoinComponent {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}