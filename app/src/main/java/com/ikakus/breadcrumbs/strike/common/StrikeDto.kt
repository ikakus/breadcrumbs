package com.ikakus.breadcrumbs.strike.common

data class StrikeDto(
    val title: String,
    val days: List<Long>,
    val status: StrikeStatus
)
