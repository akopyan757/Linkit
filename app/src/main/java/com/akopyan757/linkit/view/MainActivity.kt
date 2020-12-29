package com.akopyan757.linkit.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.akopyan757.linkit.R
import com.akopyan757.linkit.view.scope.mainScope
import com.akopyan757.linkit.view.scope.mainScopeId
import org.koin.core.KoinComponent
import org.koin.core.qualifier.named


class MainActivity : AppCompatActivity(), KoinComponent {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getKoin().getOrCreateScope(mainScopeId(), named<MainActivity>())
        setContentView(R.layout.activity_main)
    }

    override fun onDestroy() {
        super.onDestroy()

        mainScope().close()
        getKoin().deleteScope(mainScopeId())
    }
}