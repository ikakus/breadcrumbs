package com.ikakus.breadcrumbs.reminder

import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.ikakus.breadcrumbs.R
import java.util.*

class ReminderActivity : AppCompatActivity() {

    private var alarmHelper: AlarmHelper? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder)
        alarmHelper = AlarmHelper(this)
        findViewById<Button>(R.id.button)?.apply {
            this.setOnClickListener {

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
}