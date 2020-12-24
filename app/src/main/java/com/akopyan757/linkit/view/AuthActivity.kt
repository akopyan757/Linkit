package com.akopyan757.linkit.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.akopyan757.base.model.ApiResponse
import com.akopyan757.linkit.R
import com.akopyan757.linkit.view.service.IAuthorizationService
import kotlinx.android.synthetic.main.activity_auth.*
import org.koin.core.KoinComponent
import org.koin.core.parameter.parametersOf

class AuthActivity : AppCompatActivity(), KoinComponent {

    private val mAuthorizationService: IAuthorizationService by lazy {
        getKoin().get { parametersOf(this) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        btnAuthHuawei.setOnClickListener {
            mAuthorizationService.signIn()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (mAuthorizationService.onActivityResult(requestCode, resultCode, data)) {
            is ApiResponse.Success -> {
                Log.i(TAG, "onActivityResult: HUAWEI")
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }

            else -> {
                Log.i(TAG, "onActivityResult: else")
            }
        }
    }

    companion object {
        const val TAG = "AUTH_ACTIVITY"
    }
}