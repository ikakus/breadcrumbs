package com.ikakus.breadcrumbs.utils

import java.util.*

fun Calendar.getDay(): Int {
    return get(Calendar.DAY_OF_YEAR)
}

fun Calendar.getMinute(): Int {
    return get(Calendar.MINUTE)
}
