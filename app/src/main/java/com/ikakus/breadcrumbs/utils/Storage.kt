package com.ikakus.breadcrumbs.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

private const val FIRST_CHECK_DAY = "firstCheckDay"
private const val DB_NAME = "Days"
private const val LAST_CHECK_DAY = "lastCheckDay"
private const val TITLE = "title"
private const val ACTIVE = "active"
private const val DAYS = "days"
private const val HISTORY = "history"

class Storage(context: Context) {

    private var sharedPref =
        context.getSharedPreferences(DB_NAME, Context.MODE_PRIVATE)

    fun resetStrike() {
        with(sharedPref.edit()) {
            putString(TITLE, "")
            putLong(LAST_CHECK_DAY, -1)
            putString(DAYS, "")
            putBoolean(ACTIVE, false)
            commit()
        }
    }

    fun saveFirstCheckday() {
        with(sharedPref.edit()) {
            putLong(FIRST_CHECK_DAY, Date().time)
            commit()
        }
    }

    fun getFirstCheckedDay(): Date {
        return Date(sharedPref.getLong(FIRST_CHECK_DAY, Date().time))
    }

    fun saveLastCheckday() {
        with(sharedPref.edit()) {
            putLong(LAST_CHECK_DAY, Date().time)
            commit()
        }
    }

    fun getLastCheckedDay(): Date? {
        val millis = sharedPref.getLong(LAST_CHECK_DAY, -1)
        return if (millis < 0) {
            null
        } else {
            Date(millis)
        }
    }

    fun setTitle(title: String) {
        with(sharedPref.edit()) {
            putString(title, title)
            commit()
        }
    }

    fun getTitle(): String {
        return sharedPref.getString(TITLE, "").orEmpty()
    }

    fun getActive(): Boolean {
        return sharedPref.getBoolean(ACTIVE, false)
    }

    fun setActive(active: Boolean) {
        with(sharedPref.edit()) {
            putBoolean(ACTIVE, active)
            commit()
        }
    }

    fun saveDays(days: MutableList<Boolean>) {
        with(sharedPref.edit()) {
            val jsonText: String = com.google.gson.Gson().toJson(days)
            putString(DAYS, jsonText)
            commit()
        }
    }

    fun getDays(): List<Boolean> {
        val savedString = sharedPref.getString(DAYS, null)
        savedString?.let {
            val type = object : TypeToken<List<Boolean>>() {}.type
            return Gson().fromJson(savedString, type)
        } ?: return emptyList()
    }

    fun addToHistory(days: List<Boolean>) {
        val history: List<List<Boolean>> = arrayListOf(days)
        with(sharedPref.edit()) {
            val jsonText: String = com.google.gson.Gson().toJson(history)
            putString(HISTORY, jsonText)
            commit()
        }
    }

    fun getHistory(): List<List<Boolean>> {
        val savedString = sharedPref.getString(HISTORY, null)
        savedString?.let {
            val type = object : TypeToken<List<List<Boolean>>>() {}.type
            return Gson().fromJson(savedString, type)
        } ?: return emptyList()
    }
}