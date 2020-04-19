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


class MainActivity : AppCompatActivity() {

    private lateinit var storage: Storage
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        storage = Storage(this)
        setScreens()
    }

    private fun setScreens() {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val active = storage.getActive()
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

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            setScreens()
        }
    }
}