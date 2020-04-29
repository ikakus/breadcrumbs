package com.ikakus.breadcrumbs.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ikakus.breadcrumbs.strike.StrikeDto
import com.ikakus.breadcrumbs.strike.StrikeStatus
import java.util.*
import kotlin.collections.ArrayList

private const val DB_NAME = "Days"
private const val HISTORY = "history"
const val STRIKELENGTH = 12

class Storage(context: Context) {
    private val repo = Repo(context)

    fun failStrike() {
        val active = repo.get().firstOrNull { it.status == StrikeStatus.ACTIVE }
        val updated = active?.copy(status = StrikeStatus.FAILED)
        updated?.let {
            repo.update(updated)
        }
    }

    fun saveFirstCheckday() {
//        with(sharedPref.edit()) {
//            putLong(FIRST_CHECK_DAY, Date().time)
//            commit()
//        }
    }

    fun getFirstCheckedDay(): Date {
//        return Date(sharedPref.getLong(FIRST_CHECK_DAY, Date().time))
        return Date()
    }

    fun saveLastCheckday() {
//        with(sharedPref.edit()) {
//            putLong(LAST_CHECK_DAY, Date().time)
//            commit()
//        }
    }

    fun getLastCheckedDay(): Date? {
//        val millis = sharedPref.getLong(LAST_CHECK_DAY, -1)
//        return if (millis < 0) {
//            null
//        } else {
//            Date(millis)
//        }
        return Date()
    }

    fun getTitle(): String {
        return repo.get().firstOrNull { it.status == StrikeStatus.ACTIVE }?.title.orEmpty()
    }

    fun hasActive(): Boolean {
        val active = repo.get().find { it.status == StrikeStatus.ACTIVE }
        return active != null
    }

    fun getDays(): List<Long> {
        return repo.get().firstOrNull { it.status == StrikeStatus.ACTIVE }?.days ?: emptyList()
    }

    fun create(title: String) {
        val days = initDays()

        val strike = StrikeDto(
            title = title,
            status = StrikeStatus.ACTIVE,
            days = days
        )
        repo.put(strike)
    }

    private fun initDays(): MutableList<Long> {
        val days = mutableListOf<Long>()
        for (a in 1..STRIKELENGTH) {
            days.add(-1)
        }
        return days
    }

}

class Repo(context: Context) {

    private var sharedPref =
        context.getSharedPreferences(DB_NAME, Context.MODE_PRIVATE)

    fun put(strike: StrikeDto) {
        val history: ArrayList<StrikeDto> = ArrayList(get())
        history.add(strike)
        putWhole(history)
    }

    fun update(strike: StrikeDto) {
        val restOfList = get().filter { it.status != StrikeStatus.ACTIVE }.toMutableList()
        restOfList.add(strike)
        putWhole(restOfList)
    }

    fun get(): List<StrikeDto> {
        val savedString = sharedPref.getString(HISTORY, null)
        savedString?.let {
            val type = object : TypeToken<List<StrikeDto>>() {}.type
            return Gson().fromJson(savedString, type)
        } ?: return emptyList()
    }

    private fun putWhole(history: List<StrikeDto>) {
        with(sharedPref.edit()) {
            val jsonText: String = Gson().toJson(history)
            putString(HISTORY, jsonText)
            commit()
        }
    }
}