package com.ikakus.breadcrumbs

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.ikakus.breadcrumbs.strike.active.ActiveStrikeFragment
import com.ikakus.breadcrumbs.strike.newstrike.NEW_STRIKE_STARTED
import com.ikakus.breadcrumbs.strike.newstrike.NewStrikeFragment
import com.ikakus.breadcrumbs.utils.Storage
import com.ikakus.breadcrumbs.utils.getDay
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var storage: Storage
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupStorage()
        setScreens()
    }

    private fun setupStorage() {
        storage = Storage(this)
        if (checkIfFailed(storage)) {
            storage.failStrike()
        }
    }

    private fun checkIfFailed(storage: Storage): Boolean {
        val lastDay = storage.getLastCheckedDay()
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
        val active = storage.hasActive()
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
        val filter = IntentFilter(NEW_STRIKE_STARTED)
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, filter)
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