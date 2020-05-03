package com.ikakus.breadcrumbs.strike.common

import com.ikakus.breadcrumbs.core.utils.getDay
import java.util.*

private const val STRIKE_LENGTH = 30

class Strike(private val repo: Repo) {

    init {
        // for first run we need initial object
        if (repo.getStrikes().isEmpty()) {
            repo.put(StrikeDto())
        }
    }

    fun checkState(date: Date) {
        if (getActive() == null) return
        if (getLastCheckedDay() == null) return
        val lastDay = getLastCheckedDay()
        val calendarLastday = Calendar.getInstance().apply {
            time = lastDay
        }

        val calendarToday = Calendar.getInstance().apply {
            time = date
        }
        val maxDaysDiff = 1
        val result = calendarToday.getDay() - calendarLastday.getDay() > maxDaysDiff
        if (result) {
            failStrike()
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

    fun getStatus(): StrikeStatus {
        return repo.getStrikes().first().status
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

    fun getDays(): List<Long> {
        return getActive()?.days ?: emptyList()
    }

    fun initializeNew(title: String) {
        require(getActive() == null) { "There is ongoing active strike" }
        val days = initDays()

        val strike = StrikeDto(
            title = title,
            status = StrikeStatus.ACTIVE,
            dateCreated = Date().time,
            days = days
        )
        repo.update(strike)
    }

    fun getHistory(): List<StrikeDto> {
        return repo.getStrikes()
    }

    private fun getIndexToWrite(days: List<Long>): Int {
        days.forEachIndexed { index, l ->
            if (l < 0) return index
        }
        return -1
    }

    private fun getActive(): StrikeDto? {
        return repo.getStrikes().firstOrNull { it.status == StrikeStatus.ACTIVE }
    }

    private fun initDays(): MutableList<Long> {
        val days = mutableListOf<Long>()
        for (a in 1..STRIKE_LENGTH) {
            days.add(-1)
        }
        return days
    }

    private fun failStrike() {
        val active = getActive()
        val updated = active?.copy(status = StrikeStatus.FAILED)
        updated?.let {
            repo.update(updated)
        }
    }

}
