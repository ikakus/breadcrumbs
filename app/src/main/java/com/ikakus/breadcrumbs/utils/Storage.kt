package com.ikakus.breadcrumbs.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class Storage(context: Context) {

    private var sharedPref =
        context.getSharedPreferences("Days", Context.MODE_PRIVATE)

    fun resetAll() {
        with(sharedPref.edit()) {
            clear()
            commit()
        }
    }

    fun checkIfFailed(): Boolean {
        val lastDay = getLastCheckedDay()
        val calendarLastday = Calendar.getInstance().apply {
            time = lastDay
        }

        val calendarToday = Calendar.getInstance().apply {
            time = Date()
        }
        val maxDaysDiff = 1
        return calendarToday.getDay() - calendarLastday.getDay() > maxDaysDiff
    }

    fun saveFirstCheckday() {
        with(sharedPref.edit()) {
            putLong("firstCheckDay", java.util.Date().time)
            commit()
        }
    }

    fun getFirstCheckedDay(): Date {
        return Date(sharedPref.getLong("firstCheckDay", Date().time))
    }

    fun saveLastCheckday() {
        with(sharedPref.edit()) {
            putLong("lastCheckDay", java.util.Date().time)
            commit()
        }
    }

    fun getLastCheckedDay(): Date {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -1)
        return Date(sharedPref.getLong("lastCheckDay", cal.time.time))
    }

    fun setTitle(title: String) {
        with(sharedPref.edit()) {
            putString("title", title)
            commit()
        }
    }

    fun getTitle(): String {
        return sharedPref.getString("title", "").orEmpty()
    }

    fun getActive(): Boolean {
        return sharedPref.getBoolean("active", false)
    }

    fun setActive(active: Boolean) {
        with(sharedPref.edit()) {
            putBoolean("active", active)
            commit()
        }
    }

    fun saveDays(days: MutableList<Boolean>) {
        with(sharedPref.edit()) {
            val jsonText: String = com.google.gson.Gson().toJson(days)
            putString("days", jsonText)
            commit()
        }
    }

    fun getDays(): List<Boolean> {
        val savedString = sharedPref.getString("days", null)
        savedString?.let {
            val type = object : TypeToken<List<Boolean>>() {}.type
            val days = Gson().fromJson<List<Boolean>>(savedString, type)

            return days.toMutableList()
        } ?: return emptyList()
    }
}