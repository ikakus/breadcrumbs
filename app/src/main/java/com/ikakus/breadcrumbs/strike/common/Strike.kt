package com.ikakus.breadcrumbs.strike.common

import java.util.*

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