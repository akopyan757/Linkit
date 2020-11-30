package com.akopyan757.linkit.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.akopyan757.linkit.R
import com.akopyan757.linkit.view.fragment.MainFragment
import org.koin.core.KoinComponent


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_main)
        val fragment = navHostFragment?.childFragmentManager?.fragments?.get(0)
        fragment?.onActivityResult(requestCode, resultCode, data)
    }
}