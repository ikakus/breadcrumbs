package com.ikakus.breadcrumbs.strike.common

data class StrikeDto(
    val dateCreated: Long = -1,
    val title: String = "",
    val days: List<Long> = emptyList(),
    val status: StrikeStatus = StrikeStatus.COLD
)
