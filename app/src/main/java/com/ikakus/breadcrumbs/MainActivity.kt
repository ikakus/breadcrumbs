package com.ikakus.breadcrumbs

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ikakus.breadcrumbs.strike.active.ActiveStrikeFragment
import com.ikakus.breadcrumbs.strike.newstrike.NewStrikeFragment
import com.ikakus.breadcrumbs.utils.Storage


class MainActivity : AppCompatActivity() {

    private lateinit var storage: Storage
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        storage = Storage(this)
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val active = storage.getActive()
        val fragment = if (active) {
            ActiveStrikeFragment()
        } else {
            NewStrikeFragment()
        }
        fragmentTransaction.add(R.id.container, fragment)
        fragmentTransaction.commit()
    }

}