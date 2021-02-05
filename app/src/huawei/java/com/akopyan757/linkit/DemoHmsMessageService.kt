package com.akopyan757.linkit

import android.util.Log
import com.huawei.hms.push.HmsMessageService
import com.huawei.hms.push.RemoteMessage

class DemoHmsMessageService : HmsMessageService() {

    companion object {
        private const val TAG = "DemoHmsMessageService"
    }

    override fun onNewToken(s: String) {
        super.onNewToken(s)
        Log.i(TAG, "onNewToken: token=$s")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.i(TAG, "onMessageReceived: data=${remoteMessage.data}")
    }
}