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

    init {
        for (a in 1..strikeLength) {
            array.add(false)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        array = getDays()

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

    private fun updateCounter() {
        findViewById<TextView>(R.id.count).apply {
            val count = array.count { it }
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

    private fun saveDays() {
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            val jsonText: String = Gson().toJson(array)
            putString("days", jsonText)
            commit()
        }
    }

    private fun getDays(): MutableList<Boolean> {
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        val savedString = sharedPref.getString("days", "")
        val turnsType = object : TypeToken<List<Boolean>>() {}.type
        val turns = Gson().fromJson<List<Boolean>>(savedString, turnsType)

        return turns.toMutableList()
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
