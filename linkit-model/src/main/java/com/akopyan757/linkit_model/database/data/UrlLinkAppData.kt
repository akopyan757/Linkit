package com.akopyan757.linkit_model.database.data

import androidx.room.ColumnInfo

data class UrlLinkAppData(
    @ColumnInfo(name = "google_app_id") var appId: String?,
    @ColumnInfo(name = "google_app_name") var appName: String?,
    @ColumnInfo(name = "google_app_url") var url: String?,
)