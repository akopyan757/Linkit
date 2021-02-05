package com.akopyan757.linkit.view

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.akopyan757.linkit.R
import com.huawei.agconnect.config.AGConnectServicesConfig
import com.huawei.hms.aaid.HmsInstanceId
import com.huawei.hms.common.ApiException
import org.koin.core.KoinComponent
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class AuthActivity : AppCompatActivity(), KoinComponent {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        getToken()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val navHostFragment = supportFragmentManager.fragments.first() as? NavHostFragment ?: return
        val childFragments = navHostFragment.childFragmentManager.fragments
        childFragments.forEach { fragment ->
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }


    private fun getToken() {
        // Create a thread.
        object : Thread() {
            override fun run() {
                try {
                    val appId = AGConnectServicesConfig.fromContext(this@AuthActivity).getString("client/app_id")
                    val tokenScope = "HCM"
                    val token = HmsInstanceId.getInstance(this@AuthActivity).getToken(appId, tokenScope).let {
                        URLEncoder.encode("Bearer $it", "UTF-8")
                    }

                    Log.i(TAG, "get token: $token")
                } catch (e: ApiException) {
                    Log.e(TAG, "get token failed, $e")
                }
            }
        }.start()
    }

    companion object {
        private const val TAG = "AUTH_ACTIVITY"
    }
}