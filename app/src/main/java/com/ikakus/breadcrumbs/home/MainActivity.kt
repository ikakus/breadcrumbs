package com.ikakus.breadcrumbs.home

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.ikakus.breadcrumbs.R
import com.ikakus.breadcrumbs.common.utils.getDay
import com.ikakus.breadcrumbs.history.HistoryActivity
import com.ikakus.breadcrumbs.strike.active.ActiveStrikeFragment
import com.ikakus.breadcrumbs.strike.common.Repo
import com.ikakus.breadcrumbs.strike.common.Strike
import com.ikakus.breadcrumbs.strike.newstrike.NEW_STRIKE_STARTED
import com.ikakus.breadcrumbs.strike.newstrike.NewStrikeFragment
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var strike: Strike
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private fun setupStrike() {
        val repo = Repo(this)
        strike = Strike(repo)
        if (strike.isActive().not()) return

        if (checkIfFailed(strike)) {
            strike.failStrike()
        }
    }

    private fun checkIfFailed(strike: Strike): Boolean {
        val lastDay = strike.getLastCheckedDay()
        val calendarLastday = Calendar.getInstance().apply {
            time = lastDay ?: Date()
        }

        val calendarToday = Calendar.getInstance().apply {
            time = Date()
        }
        val maxDaysDiff = 1
        return calendarToday.getDay() - calendarLastday.getDay() > maxDaysDiff
    }

    private fun setScreens() {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val active = strike.isActive()
        val fragment = if (active) {
            ActiveStrikeFragment()
        } else {
            NewStrikeFragment()
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