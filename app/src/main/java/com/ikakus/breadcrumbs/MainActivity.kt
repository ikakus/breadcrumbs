package com.ikakus.breadcrumbs

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private var days: MutableList<Boolean> = mutableListOf()
    private val strikeLength = 30
    private var checkPosition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initDays()
        initTitle()
        updateCounter()

        viewManager = GridLayoutManager(this, 6)
        viewAdapter = DaysRecyclerViewAdapter(days)

        recyclerView = findViewById<RecyclerView>(R.id.recycler).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        setListeners()
    }

    private fun initTitle() {
        findViewById<EditText>(R.id.title).apply {
            val title = loadTitle()
            if(title.isNotEmpty()){
                this.setText(title)
            }
        }
    }

    private fun setListeners() {
        findViewById<FloatingActionButton>(R.id.fab).apply {
            this.setOnClickListener {
                check()
            }

            this.setOnLongClickListener {
                unCheck()
                true
            }
        }

        findViewById<EditText>(R.id.title).apply {
            addTextChangedListener(object : TextWatcher {
                private var timer: Timer = Timer()
                private val DELAY: Long = 1000 // milliseconds


                override fun afterTextChanged(s: Editable?) {
                    val title = s.toString()
                    timer.cancel()
                    timer = Timer()
                    timer.schedule(
                        object : TimerTask() {
                            override fun run() {
                                saveTitle(title)
                                runOnUiThread {
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Title saved",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        },
                        DELAY
                    )
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

            })
        }
    }

    private fun initDays() {
        if (isFirstRun()) {
            for (a in 1..strikeLength) {
                days.add(false)
            }
            setFitstRun(false)
        } else {
            days = getDays().toMutableList()
        }
    }

    private fun updateCounter() {
        findViewById<TextView>(R.id.count).apply {
            val count = days.count { it }
            checkPosition = count - 1
            text = "$count/$strikeLength"
        }
    }

    private fun unCheck() {
        if (checkPosition > -1) {
            days[checkPosition] = false
            decrementPosition()
            updateCounter()
            viewAdapter.notifyDataSetChanged()
            saveDays()
        }
    }

    private fun check() {
        incrementPosition()
        days[checkPosition] = true
        updateCounter()
        viewAdapter.notifyDataSetChanged()
        saveDays()
    }

    private fun isFirstRun(): Boolean {
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        return sharedPref.getBoolean("firstRun", true)
    }

    private fun setFitstRun(firstRun: Boolean) {
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean("firstRun", firstRun)
            commit()
        }
    }


    private fun saveTitle(title: String) {
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("title", title)
            commit()
        }
    }

    private fun loadTitle(): String{
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        return sharedPref.getString("title", "").orEmpty()
    }

    private fun saveDays() {
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            val jsonText: String = Gson().toJson(days)
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
        } ?: return emptyList()
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
