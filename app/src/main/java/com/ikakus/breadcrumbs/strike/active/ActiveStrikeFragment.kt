package com.ikakus.breadcrumbs.strike.active

import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ikakus.breadcrumbs.R
import com.ikakus.breadcrumbs.reminder.AlarmHelper
import com.ikakus.breadcrumbs.utils.Storage
import java.text.SimpleDateFormat
import java.util.*

class ActiveStrikeFragment : Fragment() {

    private var recyclerView: RecyclerView? = null
    private lateinit var viewAdapter: DaysRecyclerViewAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    private var days: MutableList<Boolean> = mutableListOf()
    private val strikeLength = 12
    private var checkPosition = 1

    private lateinit var storage: Storage

    private var alarmHelper: AlarmHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewManager = GridLayoutManager(requireContext(), 6)

        storage = Storage(requireContext())
        alarmHelper = AlarmHelper(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_active_strike, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupStorage()
        days = initDays()
        initRecycler(days)
        initTitle()
        updateCounter()
        setListeners()
        checkButtonState()
    }

    private fun setupStorage() {
        if (storage.checkIfFailed()) {
            storage.resetAll()
        }
    }

    private fun checkButtonState() {
        view?.findViewById<Button>(R.id.button_check)?.apply {
            isEnabled = !DateUtils.isToday(storage.getLastCheckedDay().time)

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
        view?.findViewById<EditText>(R.id.title)?.apply {
            val title = storage.loadTitle()
            if (title.isNotEmpty()) {
                this.setText(title)
            }
        }
    }

    private fun setListeners() {
        view?.findViewById<Button>(R.id.button_check)?.apply {
            this.setOnClickListener {
                check()
            }

            this.setOnLongClickListener {
                unCheck()
                true
            }
        }

        view?.findViewById<Button>(R.id.button_timer)?.apply {
            this.setOnClickListener {
                // Get Current Date
                // Navigate to activity
                val cal = Calendar.getInstance()
                val hour = cal.get(Calendar.HOUR_OF_DAY)
                val min = cal.get(Calendar.MINUTE)
                Log.d("tag", cal.toString())
                val datePickerDialog = TimePickerDialog(
                    context,
                    TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                        Calendar.getInstance().apply {
                            set(Calendar.HOUR_OF_DAY, hourOfDay)
                            set(Calendar.MINUTE, minute)
                            alarmHelper?.setAlaram(this)
                        }

                    },
                    hour,
                    min,
                    true
                )
                datePickerDialog.show()
            }

        }

        view?.findViewById<EditText>(R.id.title)?.apply {
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
                                requireActivity().runOnUiThread {
                                    Toast.makeText(
                                        requireContext(),
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

    private fun initRecycler(days: MutableList<Boolean>) {

        viewAdapter = DaysRecyclerViewAdapter(
            days,
            DateUtils.isToday(storage.getLastCheckedDay().time)
        )

        recyclerView = view?.findViewById<RecyclerView>(R.id.recycler)?.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        view?.findViewById<TextView>(R.id.first_date)?.apply {
            val date = storage.getFirstCheckedDay()
            val dateFormat = SimpleDateFormat("dd.MM.YYYY", Locale.getDefault())
            text = dateFormat.format(date)
        }

    }

    private fun initDays(): MutableList<Boolean> {
        var days = mutableListOf<Boolean>()
        if (storage.getDays().isEmpty()) {
            for (a in 1..strikeLength) {
                days.add(false)
            }
        } else {
            days = storage.getDays().toMutableList()
        }
        return days
    }

    private fun updateCounter() {
        view?.findViewById<TextView>(R.id.count)?.apply {
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