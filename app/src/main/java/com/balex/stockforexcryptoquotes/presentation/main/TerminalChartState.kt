package com.balex.stockforexcryptoquotes.presentation.main

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import com.balex.stockforexcryptoquotes.domain.entity.Asset
import com.balex.stockforexcryptoquotes.domain.entity.AssetList
import com.balex.stockforexcryptoquotes.domain.entity.Bar
import kotlinx.parcelize.Parcelize
import kotlin.math.roundToInt

@Parcelize
data class TerminalChartState(
    val barList: List<Bar>,
    val visibleBarsCount: Int = 100,
    val terminalWidth: Float = 1f,
    val terminalHeight: Float = 2f,
    val scrolledBy: Float = 0f
) : Parcelable {
    val barWidth: Float
        get() = terminalWidth / visibleBarsCount
    private val visibleBars: List<Bar>
        get() {
            val startIndex = (scrolledBy / barWidth).roundToInt().coerceAtLeast(0)
            val endIndex = (startIndex + visibleBarsCount).coerceAtMost(barList.size)
            return barList.subList(startIndex, endIndex)
        }
    val max: Float
        get() = visibleBars.maxOf { it.high }
    val min: Float
        get() = visibleBars.minOf { it.low }
    val pxPerPoint: Float
        get() = terminalHeight / (max - min)
}
@Composable
fun rememberTerminalChartState(
    bars: List<Bar>,
    width: Float,
    height: Float
): MutableState<TerminalChartState> {
    return rememberSaveable(bars.hashCode()) {
        mutableStateOf(TerminalChartState(
            barList = bars,
            terminalWidth = width,
            terminalHeight = height
        ))
    }
}