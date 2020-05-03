package com.ikakus.breadcrumbs.home

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Button
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.ikakus.breadcrumbs.R
import com.ikakus.breadcrumbs.core.base.BaseActivity
import com.ikakus.breadcrumbs.history.HistoryActivity
import com.ikakus.breadcrumbs.strike.active.ActiveStrikeFragment
import com.ikakus.breadcrumbs.strike.common.Repo
import com.ikakus.breadcrumbs.strike.common.Strike
import com.ikakus.breadcrumbs.strike.common.StrikeStatus
import com.ikakus.breadcrumbs.strike.done.DoneFragment
import com.ikakus.breadcrumbs.strike.failed.FailedFragment
import com.ikakus.breadcrumbs.strike.newstrike.NEW_STRIKE_STARTED
import com.ikakus.breadcrumbs.strike.newstrike.NewStrikeFragment
import java.util.*


class MainActivity : BaseActivity() {

    private lateinit var strike: Strike
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoAnimation)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private fun setupStrike() {
        val repo = Repo(this)
        strike = Strike(repo)
    }

    private fun setScreens() {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        strike.checkState(Date())
        val fragment = when (strike.getStatus()) {
            StrikeStatus.ACTIVE -> {
                ActiveStrikeFragment()
            }
            StrikeStatus.FAILED -> FailedFragment()
            StrikeStatus.DONE -> DoneFragment()
            StrikeStatus.NEW -> NewStrikeFragment()
        }
        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.commit()
    }

    override fun onResume() {
        super.onResume()
        setupStrike()
        setScreens()
        setListeners()
        val filter = IntentFilter(NEW_STRIKE_STARTED)
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, filter)
    }

    private fun setListeners() {
        findViewById<Button>(R.id.button_list)?.apply {
            this.setOnClickListener {
                startActivity(Intent(context, HistoryActivity::class.java))
            }
        }
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            setScreens()
        }
    }
}