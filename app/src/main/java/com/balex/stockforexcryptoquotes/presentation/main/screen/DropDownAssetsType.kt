package com.balex.stockforexcryptoquotes.presentation.main.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.balex.stockforexcryptoquotes.domain.entity.*
import com.balex.stockforexcryptoquotes.presentation.main.TerminalState

private val LIST_OPTIONS_TEXT_SIZE = 24.sp
private val LIST_ASSETS_TEXT_SIZE = 20.sp

@Composable
fun DropDownAssetsType(
    terminalState: State<TerminalState>,
    onTerminalStateChanged: (TerminalState) -> Unit,
    onAssetSelected: (TerminalState) -> Unit
) {
    Row {
        ShowOptionsDropMenu(terminalState, onTerminalStateChanged)
        Spacer(modifier = Modifier.width(8.dp))
        ShowAssetsDropMenu(terminalState, onAssetSelected)
    }
}

@Composable
fun ShowOptionsDropMenu(
    terminalState: State<TerminalState>,
    onTerminalStateChanged: (TerminalState) -> Unit
) {
    var isChooseOptionDropMenuExpanded by rememberSaveable { mutableStateOf(false) }

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
                    isChooseOptionDropMenuExpanded = !isChooseOptionDropMenuExpanded
                },
                text = "Option: ${terminalState.value.selectedOption}"
            )
            Spacer(modifier = Modifier.width(4.dp))
            IconButton(onClick = {
                isChooseOptionDropMenuExpanded = !isChooseOptionDropMenuExpanded
            }) {
                Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = null)
            }
        }

        DropdownMenu(
            //expanded = terminalState.value.isChooseOptionDropMenuExpanded,
            expanded = isChooseOptionDropMenuExpanded,
            onDismissRequest = {
                isChooseOptionDropMenuExpanded = false
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
                        isChooseOptionDropMenuExpanded = false
                        onTerminalStateChanged(
                            terminalState.value.copy(
                                selectedOption = option,
                                selectedAsset = selectedAssetDefault
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
    onAssetSelected: (TerminalState) -> Unit
) {
    var isChooseAssetDropMenuExpanded by rememberSaveable { mutableStateOf(false) }
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
                    isChooseAssetDropMenuExpanded = !isChooseAssetDropMenuExpanded
                },
                text = "Asset : ${terminalState.value.selectedAsset.symbol.trim()}"
            )
            Spacer(modifier = Modifier.width(4.dp))
            IconButton(onClick = {
                isChooseAssetDropMenuExpanded = !isChooseAssetDropMenuExpanded

            }) {
                Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = null)
            }
        }

        DropdownMenu(
            expanded = isChooseAssetDropMenuExpanded,
            onDismissRequest = {
                isChooseAssetDropMenuExpanded = false
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
                            )
                        )
                        isChooseAssetDropMenuExpanded = false
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
