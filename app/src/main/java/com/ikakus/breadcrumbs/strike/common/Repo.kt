package com.ikakus.breadcrumbs.strike.common

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


private const val DB_NAME = "days"
private const val STRIKES = "strikes"

class Repo(context: Context) {

    private var sharedPref =
        context.getSharedPreferences(DB_NAME, Context.MODE_PRIVATE)

    fun put(strike: StrikeDto) {
        val history: ArrayList<StrikeDto> = ArrayList(getStrikes())
        history.add(strike)
        putList(history)
    }

    fun update(strike: StrikeDto) {
        val restOfList =
            getStrikes().filter { it.status != StrikeStatus.ACTIVE && it.status != StrikeStatus.COLD }
                .toMutableList()
        restOfList.add(strike)
        putList(restOfList)
    }

    fun getStrikes(): List<StrikeDto> {
        val savedString = sharedPref.getString(STRIKES, null)
        savedString?.let {
            val type = object : TypeToken<List<StrikeDto>>() {}.type
            return Gson().fromJson(savedString, type)
        } ?: return emptyList()
    }

    private fun putList(listOfStrikes: List<StrikeDto>) {
        with(sharedPref.edit()) {
            val jsonText: String = Gson().toJson(listOfStrikes)
            putString(STRIKES, jsonText)
            commit()
        }
    }
}