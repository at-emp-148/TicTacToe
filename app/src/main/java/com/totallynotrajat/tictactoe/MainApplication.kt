package com.totallynotrajat.tictactoe

import android.app.Application
import com.trackier.sdk.TrackierSDK
import com.trackier.sdk.TrackierSDKConfig
import java.util.logging.Level

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val TR_SDK_KEY = "placeholdergi" // Your SDK key

        val sdkConfig = TrackierSDKConfig(
            this,               // Application context
            TR_SDK_KEY,         // Your AppTrove SDK key
            "production"       // Environment: "development", "testing", or "production"
        )
        sdkConfig.setAndroidId("User Android Id") // O
        sdkConfig.setLogLevel(Level.ALL)// Optional And For Android Only

        TrackierSDK.initialize(sdkConfig)
    }
}