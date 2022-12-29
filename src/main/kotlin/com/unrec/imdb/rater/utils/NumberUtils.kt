package com.unrec.imdb.rater.utils

import kotlin.time.Duration.Companion.minutes

fun Int?.getOrZero() = this ?: 0

fun Int?.minutesToDurationString(): String {
    return when (this) {
        null -> ""
        else -> this.minutes.toString()
    }
}

