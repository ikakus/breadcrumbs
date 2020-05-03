package com.ikakus.breadcrumbs.strike.active

import android.content.Intent
import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ikakus.breadcrumbs.R
import com.ikakus.breadcrumbs.core.base.BaseFragment
import com.ikakus.breadcrumbs.reminder.ReminderActivity
import com.ikakus.breadcrumbs.strike.common.Repo
import com.ikakus.breadcrumbs.strike.common.Strike
import com.ikakus.breadcrumbs.strike.newstrike.NEW_STRIKE_STARTED
import java.text.SimpleDateFormat
import java.util.*

class ActiveStrikeFragment : BaseFragment() {

    private var recyclerView: RecyclerView? = null
    private lateinit var viewAdapter: DaysAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    private var days: MutableList<Long> = mutableListOf()
    private var totalDays = 0
    private var checkPosition = 1

    private lateinit var strike: Strike

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewManager = GridLayoutManager(requireContext(), 6)

        val repo = Repo(requireContext())
        strike = Strike(repo)
        if (strike.checkIfFailed(Date())) {
            strike.failStrike()
            // TODO replace with general intent with payload
            val intent = Intent(NEW_STRIKE_STARTED)
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
        }
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
        totalDays = days.size
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

            if (checkCount == (totalDays - 1)) {
                this.text = "Finish"
            }

            if (checkCount == (totalDays)) {
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
                startActivity(Intent(requireContext(), ReminderActivity::class.java))
            }

        }

    }

    private fun initRecycler(days: MutableList<Long>) {

        viewAdapter = DaysAdapter(
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
            text = "$count/$totalDays"
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
        if (checkPosition < totalDays) {
            checkPosition++
            return true
        }
        return false
    }

}
