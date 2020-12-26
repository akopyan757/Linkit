package com.akopyan757.linkit.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.akopyan757.linkit.R
import org.koin.core.KoinComponent

class AuthActivity : AppCompatActivity(), KoinComponent {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val navHostFragment = supportFragmentManager.fragments.first() as? NavHostFragment ?: return
        val childFragments = navHostFragment.childFragmentManager.fragments
        childFragments.forEach { fragment ->
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }
}