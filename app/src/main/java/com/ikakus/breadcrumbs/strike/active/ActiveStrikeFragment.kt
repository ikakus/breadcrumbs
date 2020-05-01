package com.ikakus.breadcrumbs.strike.active

import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ikakus.breadcrumbs.R
import com.ikakus.breadcrumbs.reminder.AlarmHelper
import com.ikakus.breadcrumbs.strike.common.Repo
import com.ikakus.breadcrumbs.strike.common.STRIKELENGTH
import com.ikakus.breadcrumbs.strike.common.Strike
import java.text.SimpleDateFormat
import java.util.*

class ActiveStrikeFragment : Fragment() {

    private var recyclerView: RecyclerView? = null
    private lateinit var viewAdapter: DaysRecyclerViewAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    private var days: MutableList<Long> = mutableListOf()
    private var checkPosition = 1

    private lateinit var strike: Strike

    private var alarmHelper: AlarmHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewManager = GridLayoutManager(requireContext(), 6)

        val repo = Repo(requireContext())
        strike = Strike(repo)
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
        days = strike.getDays().toMutableList()
        initTitle(strike)
        initRecycler(days)
        updateCounter(days)
        setListeners()
        checkButtonState()
    }

    private fun checkButtonState() {
        view?.findViewById<Button>(R.id.button_check)?.apply {
            isEnabled = !DateUtils.isToday(strike.getLastCheckedDay()?.time ?: 0)

            val checkCount = days.count { it > 0 }

            this.text = "Check"

            if (checkCount == (STRIKELENGTH - 1)) {
                this.text = "Finish"
            }

            if (checkCount == (STRIKELENGTH)) {
                this.text = "All done!"
            }
        }
    }

    private fun initTitle(strike: Strike) {
        view?.findViewById<TextView>(R.id.title)?.apply {
            val title = strike.getTitle()
            if (title.isNotEmpty()) {
                this.text = title
            }
        }
    }

    private fun setListeners() {
        view?.findViewById<Button>(R.id.button_check)?.apply {
            this.setOnClickListener {
                check()
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

    }

    private fun initRecycler(days: MutableList<Long>) {

        viewAdapter = DaysRecyclerViewAdapter(
            days,
            DateUtils.isToday(strike.getLastCheckedDay()?.time ?: 0)
        )

        recyclerView = view?.findViewById<RecyclerView>(R.id.recycler)?.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        view?.findViewById<TextView>(R.id.first_date)?.apply {
            val date = strike.getFirstCheckedDay()
            val dateFormat = SimpleDateFormat("dd.MM.YYYY", Locale.getDefault())
            text = if (date != null) {
                dateFormat.format(date)
            } else {
                dateFormat.format(Date())
            }
        }

    }

    private fun updateCounter(days: MutableList<Long>) {
        view?.findViewById<TextView>(R.id.count)?.apply {
            val count = days.count { it > 0 }
            checkPosition = count
            text = "$count/$STRIKELENGTH"
            viewAdapter.setCheckPosition(checkPosition)
        }
    }

    private fun check() {
        incrementPosition()
        days[checkPosition - 1] = Date().time
        updateCounter(days)
        strike.checkDay()
        viewAdapter.setCheckPosition(checkPosition)
        viewAdapter.today = DateUtils.isToday(strike.getLastCheckedDay()?.time ?: 0)
        viewAdapter.notifyDataSetChanged()
        checkButtonState()
    }

    private fun incrementPosition(): Boolean {
        if (checkPosition < STRIKELENGTH) {
            checkPosition++
            return true
        }
        return false
    }

}
