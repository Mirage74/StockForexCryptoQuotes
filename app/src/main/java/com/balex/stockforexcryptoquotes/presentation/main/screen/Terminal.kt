package com.balex.stockforexcryptoquotes.presentation.main.screen

import android.content.Context
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.balex.stockforexcryptoquotes.presentation.main.TerminalDropDownMenuState
import com.balex.stockforexcryptoquotes.presentation.main.TerminalScreenState
import com.balex.stockforexcryptoquotes.presentation.main.TerminalViewModel
import com.balex.stockforexcryptoquotes.presentation.main.rememberTerminalChartState
import com.balex.stockforexcryptoquotes.ui.theme.StockForexCryptoQuotesTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private val TIME_FRAME_DEFAULT = TimeFrame.HOUR_1

@Composable
fun Terminal(
    context: Context,
    modifier: Modifier = Modifier,
) {

    StockForexCryptoQuotesTheme {
        val component = getApplicationComponent()
        val viewModel: TerminalViewModel =
            viewModel(factory = component.getViewModelFactory())

        viewModel.getTokenFromStorageAndSetToRepository(context)


        val screenState = viewModel.state.collectAsState(TerminalScreenState.Initial)
        when (val currentState = screenState.value) {
            is TerminalScreenState.Content -> {

                val currentWidth =
                    LocalConfiguration.current.screenWidthDp * LocalDensity.current.density
                val currentHeight =
                    LocalConfiguration.current.screenHeightDp * LocalDensity.current.density
                val terminalChartState =
                    rememberTerminalChartState(
                        bars = currentState.barList,
                        currentWidth,
                        currentHeight
                    )

                val dropDownMenuState =
                    rememberSaveable(currentState.selectedAsset.hashCode()) {
                        mutableStateOf(
                            TerminalDropDownMenuState(
                                selectedOption = currentState.selectedOption,
                                selectedAsset = currentState.selectedAsset
                            )
                        )
                    }


                Chart(
                    modifier = modifier,
                    terminalChartState = terminalChartState,
                    onTerminalChartStateChanged = {
                        terminalChartState.value = it
                    },
                    timeFrame = currentState.timeFrame
                )


                Column {
                    TimeFrames(
                        selectedFrame = currentState.timeFrame,
                        onTimeFrameSelected = {
                            viewModel.refreshQuotes(
                                it,
                                dropDownMenuState.value.selectedAsset,
                                dropDownMenuState.value.selectedOption,
                                currentState.isUserTokenSelected
                            )
                        }
                    )


                    DropDownAssetsType(
                        dropDownMenuState = dropDownMenuState,
                        onDropDownMenuStateChanged = {
                            dropDownMenuState.value = it
                        },
                        onAssetSelected = {
                            dropDownMenuState.value = it
                            viewModel.refreshQuotes(
                                currentState.timeFrame,
                                dropDownMenuState.value.selectedAsset,
                                dropDownMenuState.value.selectedOption,
                                currentState.isUserTokenSelected
                            )
                        }
                    )
                    RadioButtonWithTextField(
                        isUserTokenSelected = currentState.isUserTokenSelected,
                        currentToken = currentState.userToken,
                        onSelectedButtonChanged = {
                            viewModel.changeRadioButtonSelected()
                        },
                        onTokenSaved = {
                            viewModel.saveTokenToStorageAndRepository(context, it)
                            viewModel.refreshQuotes(
                                currentState.timeFrame,
                                dropDownMenuState.value.selectedAsset,
                                dropDownMenuState.value.selectedOption,
                                true
                            )

                        }
                    )
                }

                currentState.barList.firstOrNull()?.let {
                    Prices(
                        modifier = modifier,
                        terminalChartState,
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
                            AssetList.STOCKS,
                            false
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



