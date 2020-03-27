package com.ikakus.breadcrumbs

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private var array: MutableList<Boolean> = mutableListOf()
    private val strikeLength = 30
    private var checkPosition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        initialize()

        viewManager = GridLayoutManager(this, 6)
        viewAdapter = DaysRecyclerViewAdapter(array)

        recyclerView = findViewById<RecyclerView>(R.id.recycler).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        updateCounter()

        fab.setOnClickListener {
            check()
        }

        fab.setOnLongClickListener {
            unCheck()
            true
        }
    }

    private fun initialize() {
        if (isFirstRun()) {
            for (a in 1..strikeLength) {
                array.add(false)
            }
            setFitstRun(false)
        } else {
            array = getDays().toMutableList()
        }
    }

    private fun updateCounter() {
        findViewById<TextView>(R.id.count).apply {
            val count = array.count { it }
            checkPosition = count - 1
            text = "$count/$strikeLength"
        }
    }

    private fun unCheck() {
        if (checkPosition > -1) {
            array[checkPosition] = false
            decrementPosition()
            updateCounter()
            viewAdapter.notifyDataSetChanged()
            saveDays()
        }
    }

    private fun check() {
        incrementPosition()
        array[checkPosition] = true
        updateCounter()
        viewAdapter.notifyDataSetChanged()
        saveDays()
    }

    private fun isFirstRun(): Boolean {
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        return sharedPref.getBoolean("firstRun", true)
    }

    private fun setFitstRun(firstRun:Boolean){
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean("firstRun", firstRun)
            commit()
        }
    }

    private fun saveDays() {
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            val jsonText: String = Gson().toJson(array)
            putString("days", jsonText)
            commit()
        }
    }

    private fun getDays(): List<Boolean> {
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        val savedString = sharedPref.getString("days", null)
        savedString?.let {
            val type = object : TypeToken<List<Boolean>>() {}.type
            val days = Gson().fromJson<List<Boolean>>(savedString, type)

            return days.toMutableList()
        }?: return  emptyList()
    }

    private fun decrementPosition(): Boolean {
        if (checkPosition > -1) {
            checkPosition--
            return true
        }
        return false
    }

    private fun incrementPosition(): Boolean {
        if (checkPosition < strikeLength - 1) {
            checkPosition++
            return true
        }
        return false
    }
}
