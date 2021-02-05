package com.akopyan757.linkit

import android.content.Context
import android.view.View
import com.huawei.hms.ads.AdListener
import com.huawei.hms.ads.AdParam
import com.huawei.hms.ads.HwAds
import com.huawei.hms.ads.banner.BannerView


object BannerViewExtension {

    private const val TAG = "BANNER_VIEW_EXT"

    fun init(context: Context) {
        HwAds.init(context)
    }

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