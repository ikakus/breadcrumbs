package com.ikakus.breadcrumbs.strike

data class StrikeDto(
    val title: String,
    val days: List<Long>,
    val status: StrikeStatus
)

enum class StrikeStatus {
    ACTIVE,
    FAILED,
    DONE
}