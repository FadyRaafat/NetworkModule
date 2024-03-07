package com.blabs.network

import android.app.Application
//import com.datadog.android.Datadog
//import com.datadog.android.DatadogSite
//import com.datadog.android.core.configuration.Configuration
//import com.datadog.android.core.configuration.Credentials
//import com.datadog.android.privacy.TrackingConsent
//import com.datadog.android.rum.Rum
//import com.datadog.android.rum.RumConfiguration
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ApplicationClass : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}