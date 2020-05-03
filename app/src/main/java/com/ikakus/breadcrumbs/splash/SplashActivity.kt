package com.ikakus.breadcrumbs.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.ikakus.breadcrumbs.core.base.BaseActivity
import com.ikakus.breadcrumbs.home.MainActivity

class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scheduleSplashScreen()
    }

    private fun scheduleSplashScreen() {
        val splashScreenDuration = 1000L
        Handler().postDelayed(
            {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            },
            splashScreenDuration
        )
    }
}