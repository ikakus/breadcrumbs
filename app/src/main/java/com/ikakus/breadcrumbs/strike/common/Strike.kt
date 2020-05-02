package com.ikakus.breadcrumbs.strike.common

import java.util.*

private const val STRIKE_LENGTH = 12

class Strike(private val repo: Repo) {

    fun failStrike() {
        val active = getActive()
        val updated = active?.copy(status = StrikeStatus.FAILED)
        updated?.let {
            repo.update(updated)
        }
    }

    fun getFirstCheckedDay(): Date? {
        val strike = getActive()
        require(strike != null) { "No active strike" }
        val day = strike.days.firstOrNull { it > 0 } ?: return null
        return Date(day)
    }

    fun getLastCheckedDay(): Date? {
        val strike = getActive()
        require(strike != null) { "No active strike" }
        val day = strike.days.lastOrNull { it > 0 } ?: return null
        return Date(day)
    }

    fun getTitle(): String {
        val strike = getActive()
        require(strike != null) { "No active strike" }
        return strike.title
    }

    fun isActive(): Boolean {
        return getActive() != null
    }

    fun checkDay() {
        val active = getActive()
        active?.let {
            val index = getIndexToWrite(active.days)
            val days = active.days.toMutableList()
            days[index] = Date().time
            repo.update(active.copy(days = days))
        }
    }

    private fun getIndexToWrite(days: List<Long>): Int {
        days.forEachIndexed { index, l ->
            if (l < 0) return index
        }
        return -1
    }

    fun getDays(): List<Long> {
        return getActive()?.days ?: emptyList()
    }

    fun initializeNew(title: String) {
        require(getActive() == null) { "There is ongoing active strike" }
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
        for (a in 1..STRIKE_LENGTH) {
            days.add(-1)
        }
        return days
    }

    fun getHistory(): List<StrikeDto> {
        return repo.get()
    }

}