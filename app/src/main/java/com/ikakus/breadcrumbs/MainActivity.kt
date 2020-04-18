package com.ikakus.breadcrumbs

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ikakus.breadcrumbs.strike.active.ActiveStrikeFragment


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment = ActiveStrikeFragment()
        fragmentTransaction.add(R.id.container, fragment)
        fragmentTransaction.commit()
    }

}