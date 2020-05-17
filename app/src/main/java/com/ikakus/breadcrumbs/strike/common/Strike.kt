package com.ikakus.breadcrumbs.strike.common

import com.ikakus.breadcrumbs.core.utils.getDay
import java.util.*

private const val STRIKE_LENGTH = 30

class Strike(private val strikeRepository: StrikeRepository) {

    init {
        // for first run we need initial object
        if (strikeRepository.getStrikes().isEmpty()) {
            initializeNew()
        }
    }

    fun checkState(date: Date) {
        if (getCurrent().status != StrikeStatus.ONGOING) return
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
        val strike = getCurrent()
        val day = strike.days.firstOrNull { it > 0 } ?: return null
        return Date(day)
    }

    fun getLastCheckedDay(): Date? {
        val strike = getCurrent()
        val day = strike.days.lastOrNull { it > 0 } ?: return null
        return Date(day)
    }

    fun getTitle(): String {
        val strikes = strikeRepository.getStrikes().sortedByDescending { it.dateCreated }
        return strikes.first().title
    }


    fun checkDay() {
        val active = getCurrent()
        active.let {
            val index = getIndexToWrite(active.days)
            val days = active.days.toMutableList()
            days[index] = Date().time
            strikeRepository.update(active.copy(days = days))
        }
    }

    fun getDays(): List<Long> {
        return getCurrent().days
    }

    fun initializeActive(title: String) {
        val days = initDays()

        val strike = StrikeDto(
            title = title,
            status = StrikeStatus.ONGOING,
            dateCreated = Date().time,
            days = days
        )
        strikeRepository.update(strike)
    }

    fun initializeNew() {
        strikeRepository.put(StrikeDto(dateCreated = Date().time))
    }

    fun getHistory(): List<StrikeDto> {
        return strikeRepository.getStrikes()
    }

    private fun getIndexToWrite(days: List<Long>): Int {
        days.forEachIndexed { index, l ->
            if (l < 0) return index
        }
        return -1
    }

    fun getCurrent(): StrikeDto {
        val strikes = strikeRepository.getStrikes().sortedByDescending { it.dateCreated }
        return strikes.first()
    }

    private fun initDays(): MutableList<Long> {
        val days = mutableListOf<Long>()
        for (a in 1..STRIKE_LENGTH) {
            days.add(-1)
        }
        return days
    }

    private fun failStrike() {
        val active = getCurrent()
        val updated = active.copy(status = StrikeStatus.FAILED)
        updated.let {
            strikeRepository.update(updated)
        }
    }

}
