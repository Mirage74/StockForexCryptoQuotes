package com.balex.stockforexcryptoquotes.presentation.main

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.balex.stockforexcryptoquotes.R
import com.balex.stockforexcryptoquotes.domain.entity.Asset
import com.balex.stockforexcryptoquotes.domain.entity.AssetList
import com.balex.stockforexcryptoquotes.domain.entity.Bar
import com.balex.stockforexcryptoquotes.domain.entity.CryptoList
import com.balex.stockforexcryptoquotes.domain.entity.ForexPairList
import com.balex.stockforexcryptoquotes.domain.entity.StockList
import com.balex.stockforexcryptoquotes.domain.entity.TimeFrame
import com.balex.stockforexcryptoquotes.presentation.getApplicationComponent
import com.balex.stockforexcryptoquotes.ui.theme.StockForexCryptoQuotesTheme
import java.time.Year
import java.util.Calendar
import java.util.Locale
import kotlin.math.roundToInt


private const val MIN_VISIBLE_BARS_COUNT = 20

@Composable
fun Terminal(
    modifier: Modifier = Modifier
) {

    StockForexCryptoQuotesTheme {
        val component = getApplicationComponent()
        val viewModel: TerminalViewModel =
            viewModel(factory = component.getViewModelFactory())
        val screenState = viewModel.state.collectAsState(TerminalScreenState.Initial)
        when (val currentState = screenState.value) {
            is TerminalScreenState.Content -> {
                val currentWidth =
                    LocalConfiguration.current.screenWidthDp * LocalDensity.current.density
                val currentHeight =
                    LocalConfiguration.current.screenHeightDp * LocalDensity.current.density
                val terminalState =
                    rememberTerminalState(
                        bars = currentState.barList,
                        selectedOption = currentState.selectedOption,
                        selectedAsset = currentState.selectedAsset,
                        currentWidth,
                        currentHeight
                    )
                Chart(
                    modifier = modifier,
                    terminalState = terminalState,
                    onTerminalStateChanged = {
                        terminalState.value = it
                    },
                    timeFrame = currentState.timeFrame
                )

                Column {
                    Log.d("Terminal", "TimeFrames")

                    TimeFrames(
                        selectedFrame = currentState.timeFrame,
                        onTimeFrameSelected = {
                            viewModel.refreshQuotes(
                                it,
                                terminalState.value.selectedAsset,
                                terminalState.value.selectedOption
                            )
                        }
                    )
                    DropDownAssetsType(
                        terminalState,
                        onTerminalStateChanged = {
                            terminalState.value = it
                        },
                        onAssetSelected = {
                            terminalState.value = it
                            viewModel.refreshQuotes(
                                currentState.timeFrame,
                                terminalState.value.selectedAsset,
                                terminalState.value.selectedOption
                            )
                        }
                    )

                    RadioButtonWithTextField()
                }


                currentState.barList.firstOrNull()?.let {
                    Prices(
                        modifier = modifier,
                        terminalState,
                        lastPrice = it.close
                    )
                }

            }

            is TerminalScreenState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is TerminalScreenState.Error -> {
                ErrorScreen(
                    onRefreshButtonClickListener = {
                        viewModel.refreshQuotes(
                            TIME_FRAME_DEFAULT,
                            Asset.DEFAULT_STOCK,
                            AssetList.STOCKS
                        )
                    }
                )
            }

            is TerminalScreenState.Initial -> {

            }
        }
    }
}

@Composable
private fun TimeFrames(
    selectedFrame: TimeFrame,
    onTimeFrameSelected: (TimeFrame) -> Unit
) {
    Row(
        modifier = Modifier
            .wrapContentSize()
            .padding(16.dp, 16.dp, 16.dp, 0.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        TimeFrame.entries.forEach { timeFrame ->
            val labelResId = when (timeFrame) {
                TimeFrame.MIN_5 -> R.string.timeframe_5_minutes
                TimeFrame.HOUR_1 -> R.string.timeframe_1_hour
                TimeFrame.DAY_1 -> R.string.timeframe_1_day
                TimeFrame.WEEK_1 -> R.string.timeframe_1_week
            }
            val isSelected = timeFrame == selectedFrame
            AssistChip(
                onClick = { onTimeFrameSelected(timeFrame) },
                label = { Text(text = stringResource(id = labelResId)) },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = if (isSelected) Color.White else Color.Black,
                    labelColor = if (isSelected) Color.Black else Color.White
                )
            )
        }
    }
}

@Composable
private fun Chart(
    modifier: Modifier = Modifier,
    terminalState: State<TerminalState>,
    onTerminalStateChanged: (TerminalState) -> Unit,
    timeFrame: TimeFrame

) {
    Log.d("Terminal", "Chart")
    val currentState = terminalState.value
    val transformableState = rememberTransformableState { zoomChange, panChange, _ ->
        val visibleBarsCount = (currentState.visibleBarsCount / zoomChange).roundToInt()
            .coerceIn(MIN_VISIBLE_BARS_COUNT, currentState.barList.size)

        val scrolledBy = (currentState.scrolledBy + panChange.x)
            .coerceAtLeast(0f)
            .coerceAtMost(currentState.barList.size * currentState.barWidth - currentState.terminalWidth)
        onTerminalStateChanged(
            currentState.copy(
                visibleBarsCount = visibleBarsCount,
                scrolledBy = scrolledBy
            )
        )
    }

    val textMeasurer = rememberTextMeasurer()

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .clipToBounds()
            .padding(
                top = 64.dp,
                bottom = 32.dp,
                end = 32.dp
            )
            .transformable(transformableState)
            .onSizeChanged {
                onTerminalStateChanged(
                    currentState.copy(
                        terminalWidth = it.width.toFloat(),
                        terminalHeight = (it.height).toFloat()
                    ),
                )
            }
    ) {
        val min = currentState.min
        val pxPerPoint = currentState.pxPerPoint



        translate(left = currentState.scrolledBy) {


            currentState.barList.forEachIndexed { index, bar ->
                val offsetX = size.width - index * currentState.barWidth

                drawLine(
                    color = Color.White,
                    start = Offset(offsetX, size.height - (bar.low - min) * pxPerPoint),
                    end = Offset(offsetX, size.height - (bar.high - min) * pxPerPoint),
                    strokeWidth = 1f
                )
                drawLine(
                    color = if (bar.open < bar.close) Color.Green else Color.Red,
                    start = Offset(offsetX, size.height - (bar.open - min) * pxPerPoint),
                    end = Offset(offsetX, size.height - (bar.close - min) * pxPerPoint),
                    strokeWidth = currentState.barWidth / 2
                )


                drawTimeDelimiter(
                    bar = bar,
                    nextBar = if (index < currentState.barList.size - 1) {
                        currentState.barList[index + 1]
                    } else {
                        null
                    },
                    timeFrame = timeFrame,
                    offsetX = offsetX,
                    textMeasurer = textMeasurer
                )

            }
        }
    }
}

@Composable
private fun Prices(
    modifier: Modifier = Modifier,
    terminalState: State<TerminalState>,
    lastPrice: Float
) {
    Log.d("Terminal", "Prices")
    val currentState = terminalState.value
    val textMeasurer = rememberTextMeasurer()
    val max = currentState.max
    val min = currentState.min
    val pxPerPoint = currentState.pxPerPoint


    Canvas(
        modifier = modifier
            .fillMaxSize()
            .clipToBounds()
            .padding(vertical = 32.dp)
    ) {

        drawPrices(max, min, pxPerPoint, lastPrice, textMeasurer)
    }
}

@Composable
private fun ErrorScreen(
    onRefreshButtonClickListener: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.wrapContentHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Error loading data",
                fontSize = 30.sp,
                color = Color.Red
            )
            Spacer(modifier = Modifier.height(100.dp))
            Text(
                text = "Please check internet connection",
                fontSize = 30.sp
            )
            Spacer(modifier = Modifier.height(100.dp))
            Button(onClick = { onRefreshButtonClickListener() })
            {
                Text(text = "Refresh")
            }

        }
    }
}

@Composable
fun DropDownAssetsType(
    terminalState: State<TerminalState>,
    onTerminalStateChanged: (TerminalState) -> Unit,
    onAssetSelected: (TerminalState) -> Unit
) {
    Row {
        ShowOptionsDropMenu(terminalState, onTerminalStateChanged)
        Spacer(modifier = Modifier.width(8.dp))
        ShowAssetsDropMenu(terminalState, onTerminalStateChanged, onAssetSelected)
    }

}


@Composable
fun ShowOptionsDropMenu(
    terminalState: State<TerminalState>,
    onTerminalStateChanged: (TerminalState) -> Unit
) {

    Column(
        modifier = Modifier
            .padding(16.dp, 0.dp, 0.dp, 0.dp)
            .background(Color.White)
            .border(width = 1.dp, color = Color.Black)
    ) {
        Row(
            modifier = Modifier
                .height(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.clickable {
                    onTerminalStateChanged(
                        terminalState.value.copy(
                            isChooseOptionDropMenuExpanded = !terminalState.value.isChooseOptionDropMenuExpanded
                        )
                    )
                },
                text = "Option: ${terminalState.value.selectedOption}"
            )
            Spacer(modifier = Modifier.width(4.dp))
            IconButton(onClick = {
                onTerminalStateChanged(
                    terminalState.value.copy(
                        isChooseOptionDropMenuExpanded = !terminalState.value.isChooseOptionDropMenuExpanded
                    )
                )
            }) {
                Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = null)
            }
        }

        DropdownMenu(
            expanded = terminalState.value.isChooseOptionDropMenuExpanded,
            onDismissRequest = {
                onTerminalStateChanged(
                    terminalState.value.copy(
                        isChooseOptionDropMenuExpanded = false
                    )
                )
            },

            modifier = Modifier
                .width(100.dp)
                .background(Color.White)
                .padding(0.dp)
                .border(1.dp, Color.Black)

        ) {

            AssetList.entries.forEach { option ->
                DropdownMenuItem(
                    modifier = Modifier
                        .padding(0.dp)
                        .border(1.dp, Color.LightGray),
                    onClick = {
                        val selectedAssetDefault = when (option) {
                            AssetList.STOCKS -> Asset.DEFAULT_STOCK
                            AssetList.FOREX -> Asset.DEFAULT_FOREX
                            AssetList.CRYPTO -> Asset.DEFAULT_CRYPTO
                        }
                        onTerminalStateChanged(
                            terminalState.value.copy(
                                selectedOption = option,
                                selectedAsset = selectedAssetDefault,
                                isChooseOptionDropMenuExpanded = false,
                                isChooseAssetDropMenuExpanded = true
                            )
                        )
                    },
                    text = {
                        Text(
                            text = option.value,
                            fontSize = LIST_OPTIONS_TEXT_SIZE
                        )
                    },
                )
            }
        }
    }
}

@Composable
fun ShowAssetsDropMenu(
    terminalState: State<TerminalState>,
    onTerminalStateChanged: (TerminalState) -> Unit,
    onAssetSelected: (TerminalState) -> Unit
) {

    Column(
        modifier = Modifier
            .padding(16.dp, 0.dp, 0.dp, 0.dp)
            .background(Color.White)
            .border(width = 1.dp, color = Color.Black)
    ) {
        Row(
            modifier = Modifier
                .height(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.clickable {
                    onTerminalStateChanged(
                        terminalState.value.copy(
                            isChooseAssetDropMenuExpanded = !terminalState.value.isChooseAssetDropMenuExpanded
                        )
                    )
                },
                text = "Asset : ${terminalState.value.selectedAsset.symbol.trim()}"
            )
            Spacer(modifier = Modifier.width(4.dp))
            IconButton(onClick = {
                onTerminalStateChanged(
                    terminalState.value.copy(
                        isChooseAssetDropMenuExpanded = !terminalState.value.isChooseAssetDropMenuExpanded
                    )
                )
            }) {
                Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = null)
            }
        }

        DropdownMenu(
            expanded = terminalState.value.isChooseAssetDropMenuExpanded,
            onDismissRequest = {
                onTerminalStateChanged(
                    terminalState.value.copy(
                        isChooseAssetDropMenuExpanded = false
                    )
                )
            },

            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(0.dp)
                .border(1.dp, Color.Black)


        ) {
            val currentAssetsList: List<Asset> = when (terminalState.value.selectedOption) {
                AssetList.STOCKS -> {
                    StockList().stockList
                }

                AssetList.FOREX -> {
                    ForexPairList().forexPairList
                }

                AssetList.CRYPTO -> {
                    CryptoList().cryptoList
                }
            }
            currentAssetsList.forEachIndexed { index, asset ->
                DropdownMenuItem(
                    modifier = Modifier
                        .padding(0.dp)
                        .border(1.dp, Color.LightGray),
                    onClick = {
                        onAssetSelected(
                            terminalState.value.copy(
                                selectedAsset = Asset(
                                    asset.symbol.trim(),
                                    asset.description.trim()
                                ),
                                isChooseAssetDropMenuExpanded = false
                            )
                        )
                    },
                    text = {
                        Text(
                            modifier = Modifier.fillMaxSize(),
                            text = "${asset.symbol}: ${asset.description}",
                            fontSize = LIST_ASSETS_TEXT_SIZE
                        )
                    },
                )
            }
        }
    }
}


@SuppressLint("DefaultLocale")
private fun DrawScope.drawTimeDelimiter(
    bar: Bar,
    nextBar: Bar?,
    timeFrame: TimeFrame,
    offsetX: Float,
    textMeasurer: TextMeasurer
) {
    val calendar = bar.calendar

    val minutes = calendar.get(Calendar.MINUTE)
    val hours = calendar.get(Calendar.HOUR_OF_DAY)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val week = calendar.get(Calendar.WEEK_OF_MONTH)
    val month = calendar.get(Calendar.MONTH)
    val year = calendar.get(Calendar.YEAR)

    val shouldDrawDelimiter = when (timeFrame) {
        TimeFrame.MIN_5 -> {
            minutes == 0
        }

        TimeFrame.HOUR_1 -> {
            val nextBarDay = nextBar?.calendar?.get(Calendar.DAY_OF_MONTH)
            (day != nextBarDay) && (day % 5 == 0)
        }

        TimeFrame.DAY_1 -> {
            day == 15 || day == 30
        }

        TimeFrame.WEEK_1 -> {
            val nextBarMonth = nextBar?.calendar?.get(Calendar.MONTH)
            month % 6 == 0 && month != nextBarMonth
        }
    }
    if (!shouldDrawDelimiter) return

    drawLine(
        color = Color.White.copy(alpha = 0.5f),
        start = Offset(offsetX, 0f),
        end = Offset(offsetX, size.height),
        strokeWidth = 1f,
        pathEffect = PathEffect.dashPathEffect(
            intervals = floatArrayOf(4.dp.toPx(), 4.dp.toPx())
        )
    )

    val nameOfMonth = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())
    val text = when (timeFrame) {
        TimeFrame.MIN_5 -> {
            String.format("%02d:00", hours)
        }

        TimeFrame.HOUR_1, TimeFrame.DAY_1 -> {
            String.format("%s %s", day, nameOfMonth)
        }

        TimeFrame.WEEK_1 -> {
            String.format("%s-%s-%s", day, nameOfMonth, year )
        }

    }
    val textLayoutResult = textMeasurer.measure(
        text = text,
        style = TextStyle(
            color = Color.White,
            fontSize = 12.sp
        )
    )
    drawText(
        textLayoutResult = textLayoutResult,
        topLeft = Offset(offsetX - textLayoutResult.size.width / 2, size.height)
    )
}

private fun DrawScope.drawPrices(
    max: Float,
    min: Float,
    pxPerPoint: Float,
    lastPrice: Float,
    textMeasurer: TextMeasurer
) {
    // max
    val maxPriceOffsetY = 0f
    drawDashedLine(
        start = Offset(0f, maxPriceOffsetY),
        end = Offset(size.width, maxPriceOffsetY),
    )
    drawTextPrice(
        textMeasurer = textMeasurer,
        price = max,
        offsetY = maxPriceOffsetY
    )

    // last price
    val lastPriceOffsetY = size.height - ((lastPrice - min) * pxPerPoint)
    drawDashedLine(
        start = Offset(0f, lastPriceOffsetY),
        end = Offset(size.width, lastPriceOffsetY),
    )
    drawTextPrice(
        textMeasurer = textMeasurer,
        price = lastPrice,
        offsetY = lastPriceOffsetY
    )

    // min
    val minPriceOffsetY = size.height
    drawDashedLine(
        start = Offset(0f, minPriceOffsetY),
        end = Offset(size.width, minPriceOffsetY),
    )
    drawTextPrice(
        textMeasurer = textMeasurer,
        price = min,
        offsetY = minPriceOffsetY
    )
}


private fun DrawScope.drawTextPrice(
    textMeasurer: TextMeasurer,
    price: Float,
    offsetY: Float
) {
    val textLayoutResult = textMeasurer.measure(
        text = price.toString(),
        style = TextStyle(
            color = Color.White,
            fontSize = 12.sp
        )
    )
    drawText(
        textLayoutResult = textLayoutResult,
        topLeft = Offset(size.width - textLayoutResult.size.width - 4.dp.toPx(), offsetY)
    )
}

private fun DrawScope.drawDashedLine(
    color: Color = Color.White,
    start: Offset,
    end: Offset,
    strokeWidth: Float = 3f
) {
    drawLine(
        color = color,
        start = start,
        end = end,
        strokeWidth = strokeWidth,
        pathEffect = PathEffect.dashPathEffect(
            intervals = floatArrayOf(
                4.dp.toPx(), 4.dp.toPx()
            )
        )
    )
}


@Composable
fun RadioButtonWithTextField() {
    var isRadioButtonChecked by remember { mutableStateOf(false) }
    var textFieldValue by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = isRadioButtonChecked,
                onClick = { isRadioButtonChecked = !isRadioButtonChecked }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Show TextField",
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (isRadioButtonChecked) {
            TextField(

                value = textFieldValue,
                onValueChange = { textFieldValue = it },
                label = { Text("Enter text") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

private val TIME_FRAME_DEFAULT = TimeFrame.HOUR_1
private val LIST_OPTIONS_TEXT_SIZE = 24.sp
private val LIST_ASSETS_TEXT_SIZE = 20.sp
