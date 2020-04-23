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
            time = lastDay ?: Date()
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

    fun getLastCheckedDay(): Date? {
        val millis = sharedPref.getLong("lastCheckDay", -1)
        if (millis < 0) {
            return null
        } else {
            return Date(millis)
        }
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
            return Gson().fromJson(savedString, type)
        } ?: return emptyList()
    }

    fun addToHistory(days: List<Boolean>) {
        val history: List<List<Boolean>> = arrayListOf(days)
        with(sharedPref.edit()) {
            val jsonText: String = com.google.gson.Gson().toJson(history)
            putString("history", jsonText)
            commit()
        }
    }

    fun getHistory(): List<List<Boolean>> {
        val savedString = sharedPref.getString("history", null)
        savedString?.let {
            val type = object : TypeToken<List<List<Boolean>>>() {}.type
            return Gson().fromJson(savedString, type)
        } ?: return emptyList()
    }
}