package com.livetv.app.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.livetv.app.ui.mobile.MobileMainActivity
import com.livetv.app.ui.tv.TvMainActivity
import com.livetv.app.utils.DeviceUtils

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (DeviceUtils.isTV(this)) {
            startActivity(Intent(this, TvMainActivity::class.java))
        } else {
            startActivity(Intent(this, MobileMainActivity::class.java))
        }
        finish()
    }
}
