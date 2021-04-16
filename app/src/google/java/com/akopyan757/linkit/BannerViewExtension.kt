package com.akopyan757.linkit

import android.content.Context
import android.view.View
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds


object BannerViewExtension {

    fun init(context: Context) {
        MobileAds.initialize(context)
    }

    fun loadAd(view: View, onClosed: (() -> Unit)? = null) {
        val adRequest = AdRequest.Builder().build()
        (view as AdView).apply {
            loadAd(adRequest)
            adListener = object : AdListener() {
                override fun onAdClosed() {
                    onClosed?.invoke()
                }
            }
        }
    }

    fun getItemBannerViewId() = R.id.itemViewBannerAd
}