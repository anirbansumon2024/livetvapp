package com.livetv.app.ui

import android.app.UiModeManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.livetv.app.ui.mobile.MobileMainActivity
import com.livetv.app.ui.tv.TvMainActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Detect if running on Android TV
        if (isAndroidTV()) {
            startActivity(Intent(this, TvMainActivity::class.java))
        } else {
            startActivity(Intent(this, MobileMainActivity::class.java))
        }
        finish()
    }

    private fun isAndroidTV(): Boolean {
        val uiModeManager = getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        return uiModeManager.currentModeType == Configuration.UI_MODE_TYPE_TELEVISION
    }
}
