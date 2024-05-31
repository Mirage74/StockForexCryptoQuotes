package com.balex.stockforexcryptoquotes.domain.entity

enum class TimeFrame(val value: String) {
    MIN_5("5/minute"),
    HOUR_1("1/hour"),
    DAY_1("1/day"),
    WEEK_1("1/week")
}