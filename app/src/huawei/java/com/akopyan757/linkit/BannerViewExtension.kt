package com.akopyan757.linkit

import android.view.View
import com.huawei.hms.ads.AdListener
import com.huawei.hms.ads.AdParam
import com.huawei.hms.ads.banner.BannerView


object BannerViewExtension {

    private const val TAG = "BANNER_VIEW_EXT"

    fun loadAd(view: View, onClosed: (() -> Unit)? = null) {
        val adParam = AdParam.Builder().build()
        (view as BannerView).apply {
            loadAd(adParam)
            adListener = object : AdListener() {
                override fun onAdClosed() {
                    onClosed?.invoke()
                }
            }
        }
    }

    fun getItemBannerViewId() = R.id.itemViewBannerAd
}