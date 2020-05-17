package com.ikakus.breadcrumbs.reminder

import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import com.ikakus.breadcrumbs.R
import com.ikakus.breadcrumbs.core.base.BaseActivity
import java.text.DateFormat.SHORT
import java.text.SimpleDateFormat
import java.util.*

class ReminderActivity : BaseActivity() {

    lateinit var alarmHelper: AlarmHelper

    lateinit var repository: ReminderRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder)

        repository = ReminderRepository(this)
        alarmHelper = AlarmHelper(this)

        setClickListener()
        setText()
    }

    private fun setText() {
        findViewById<TextView>(R.id.tv_reminder_date)?.apply {
            val savedDate = repository.getReminderDate()
            val format = SimpleDateFormat.getTimeInstance(SHORT)
            text = if (savedDate == null) {
                "No reminder set"
            } else {
                "Reminder set to     ${format.format(savedDate)}"
            }
        }
    }

    private fun setClickListener() {
        findViewById<LinearLayout>(R.id.ll_reminder)?.apply {
            this.setOnClickListener {
                timePickerDialog(context, repository).show()
            }
        }
    }

    private fun timePickerDialog(
        context: Context,
        repository: ReminderRepository
    ): TimePickerDialog {
        val cal = Calendar.getInstance()
        val savedDate = repository.getReminderDate()
        if (savedDate != null) {
            cal.time = savedDate
        }
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val min = cal.get(Calendar.MINUTE)
        return TimePickerDialog(
            context,
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                onTimeSet(hourOfDay, minute)
            },
            hour,
            min,
            true
        )
    }

    private fun onTimeSet(hourOfDay: Int, minute: Int) {
        Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hourOfDay)
            set(Calendar.MINUTE, minute)
            repository.saveReminderDate(this.time)
            alarmHelper.setAlaram(this)
            setText()
        }
    }
}