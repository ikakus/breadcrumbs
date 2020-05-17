package com.ikakus.breadcrumbs.reminder

import android.content.Context
import java.util.*

private const val REMINDER_DATE = "reminder_date"
private const val DB_NAME = "reminder_db"

class ReminderRepository(context: Context) {

    private var sharedPref =
        context.getSharedPreferences(DB_NAME, Context.MODE_PRIVATE)

    fun getReminderDate(): Date? {
        val dateLong = sharedPref.getLong(REMINDER_DATE, -1)
        return if (dateLong == -1L) {
            null
        } else {
            Date(dateLong)
        }
    }

    fun saveReminderDate(date: Date) {
        with(sharedPref.edit()) {
            putLong(REMINDER_DATE, date.time)
            commit()
        }
    }
}