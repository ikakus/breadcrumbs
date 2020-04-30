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

class Strike(private val repo: Repo) {

    fun failStrike() {
        val active = repo.get().firstOrNull { it.status == StrikeStatus.ACTIVE }
        val updated = active?.copy(status = StrikeStatus.FAILED)
        updated?.let {
            repo.update(updated)
        }
    }

    fun getFirstCheckedDay(): Date? {
        val active = getActive() ?: return null
        val day = active.days.firstOrNull { it > 0 } ?: return null
        return Date(day)
    }

    fun getLastCheckedDay(): Date? {
        val active = getActive() ?: return null
        val day = active.days.lastOrNull { it > 0 } ?: return null
        return Date(day)
    }

    fun getTitle(): String {
        return getActive()?.title.orEmpty()
    }

    fun isActive(): Boolean {
        val active = getActive()
        return active != null
    }

    fun checkDay() {
        val active = repo.get().firstOrNull { it.status == StrikeStatus.ACTIVE }
        active?.let {
            var days = active.days.toMutableList()
            days[0] = Date().time
            repo.update(active.copy(days = days))
        }
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

    private fun getActive(): StrikeDto? {
        return repo.get().firstOrNull { it.status == StrikeStatus.ACTIVE }
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