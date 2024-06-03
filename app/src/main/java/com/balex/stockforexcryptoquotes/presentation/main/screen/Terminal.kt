package com.balex.stockforexcryptoquotes.presentation.main.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.balex.stockforexcryptoquotes.domain.entity.Asset
import com.balex.stockforexcryptoquotes.domain.entity.AssetList
import com.balex.stockforexcryptoquotes.domain.entity.TimeFrame
import com.balex.stockforexcryptoquotes.presentation.getApplicationComponent
import com.balex.stockforexcryptoquotes.presentation.main.TerminalScreenState
import com.balex.stockforexcryptoquotes.presentation.main.TerminalViewModel
import com.balex.stockforexcryptoquotes.presentation.main.rememberTerminalState
import com.balex.stockforexcryptoquotes.ui.theme.StockForexCryptoQuotesTheme

private val TIME_FRAME_DEFAULT = TimeFrame.HOUR_1

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



