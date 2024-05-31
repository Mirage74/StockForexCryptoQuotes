package com.balex.stockforexcryptoquotes.domain.entity

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize
import java.util.Calendar

@Parcelize
@Immutable
data class Bar(
    val open: Float,
    val close: Float,
    val low: Float,
    val high: Float,
    val calendar: Calendar
): Parcelable