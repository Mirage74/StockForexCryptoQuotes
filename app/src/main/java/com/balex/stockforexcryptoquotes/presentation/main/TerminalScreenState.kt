package com.balex.stockforexcryptoquotes.presentation.main


import com.balex.stockforexcryptoquotes.domain.entity.Asset
import com.balex.stockforexcryptoquotes.domain.entity.AssetList
import com.balex.stockforexcryptoquotes.domain.entity.Bar
import com.balex.stockforexcryptoquotes.domain.entity.TimeFrame

sealed class TerminalScreenState {

    data object Initial : TerminalScreenState()

    data object Loading : TerminalScreenState()

    data object Error : TerminalScreenState()

    data class Content(
        val barList: List<Bar>,
        val timeFrame: TimeFrame,
        val selectedOption: AssetList,
        val selectedAsset: Asset
    ) : TerminalScreenState()

}