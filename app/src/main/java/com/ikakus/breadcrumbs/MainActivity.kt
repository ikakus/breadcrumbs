package com.ikakus.breadcrumbs

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateUtils
import android.text.format.DateUtils.isToday
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: DaysRecyclerViewAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    private var days: MutableList<Boolean> = mutableListOf()
    private val strikeLength = 12
    private var checkPosition = 1

    private lateinit var storage:Storage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewManager = GridLayoutManager(this, 6)

        storage = Storage(this)

    }

    override fun onResume() {
        super.onResume()

        if (storage.checkIfFailed()) {
            storage.resetAll()
        }

        initDays()
        initTitle()
        updateCounter()
        setListeners()
        checkButtonState()

    }

    private fun checkButtonState() {
        findViewById<Button>(R.id.button_check).apply {
            isEnabled = !isToday(storage.getLastCheckedDay().time)

            val checkCount = days.count { it }
            if (checkCount == 0) {
                this.text = "Start"
            } else {
                this.text = "Check"
            }

            if (checkCount == (strikeLength - 1)) {
                this.text = "Finish"
            }

            if (checkCount == (strikeLength)) {
                this.text = "All done!"
            }
        }
    }

    private fun initTitle() {
        findViewById<EditText>(R.id.title).apply {
            val title = storage.loadTitle()
            if (title.isNotEmpty()) {
                this.setText(title)
            }
        }
    }

    private fun setListeners() {
        findViewById<Button>(R.id.button_check).apply {
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
                                storage.saveTitle(title)
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
        if (storage.getDays().isEmpty()) {
            for (a in 1..strikeLength) {
                days.add(false)
            }
        } else {
            days = storage.getDays().toMutableList()
        }

        viewAdapter = DaysRecyclerViewAdapter(days, isToday(storage.getLastCheckedDay().time))

        recyclerView = findViewById<RecyclerView>(R.id.recycler).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        findViewById<TextView>(R.id.first_date).apply {
            val date = storage.getFirstCheckedDay()
            val dateFormat = SimpleDateFormat("dd.MM.YYYY", Locale.getDefault())
            text = dateFormat.format(date)
        }

    }

    private fun updateCounter() {
        findViewById<TextView>(R.id.count).apply {
            val count = days.count { it }
            checkPosition = count
            text = "$count/$strikeLength"
            viewAdapter.setCheckPosition(checkPosition)
        }
    }

    private fun unCheck() {
        if (checkPosition > 0) {
            days[checkPosition - 1] = false
            decrementPosition()
            updateCounter()
            viewAdapter.setCheckPosition(checkPosition)
            viewAdapter.notifyDataSetChanged()
            storage.saveDays(days)
            checkButtonState()
        }
    }

    private fun check() {
        val checkCount = days.count { it }
        if (checkCount == 0) {
            storage.saveFirstCheckday()
        }
        incrementPosition()
        days[checkPosition - 1] = true
        updateCounter()
        storage.saveDays(days)
        storage.saveLastCheckday()

        viewAdapter.setCheckPosition(checkPosition)
        viewAdapter.today = DateUtils.isToday(storage.getLastCheckedDay().time)
        viewAdapter.notifyDataSetChanged()
        checkButtonState()
    }

    private fun decrementPosition(): Boolean {
        if (checkPosition > 0) {
            checkPosition--
            return true
        }
        return false
    }

    private fun incrementPosition(): Boolean {
        if (checkPosition < strikeLength) {
            checkPosition++
            return true
        }
        return false
    }
}