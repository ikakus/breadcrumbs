package com.ikakus.breadcrumbs.strike.common

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


private const val DB_NAME = "Days"
private const val HISTORY = "history"

class Repo(context: Context) {

    private var sharedPref =
        context.getSharedPreferences(DB_NAME, Context.MODE_PRIVATE)

    fun put(strike: StrikeDto) {
        val history: ArrayList<StrikeDto> = ArrayList(get())
        history.add(strike)
        putWhole(history)
    }

    fun update(strike: StrikeDto) {
        val restOfList =
            get().filter { it.status != StrikeStatus.ACTIVE && it.status != StrikeStatus.NEW }
                .toMutableList()
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