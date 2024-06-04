package com.balex.stockforexcryptoquotes.presentation.main

import androidx.lifecycle.ViewModel
import com.balex.stockforexcryptoquotes.domain.entity.Asset
import com.balex.stockforexcryptoquotes.domain.entity.AssetList
import com.balex.stockforexcryptoquotes.domain.entity.TimeFrame
import com.balex.stockforexcryptoquotes.domain.usecases.GetQuotesUseCase
import com.balex.stockforexcryptoquotes.domain.usecases.RefreshQuotesUseCase
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class TerminalViewModel @Inject constructor(
    getQuotesUseCase: GetQuotesUseCase,
    private val refreshQuotesUseCase: RefreshQuotesUseCase
) : ViewModel() {

    private val quotesFlow = getQuotesUseCase()

    fun refreshQuotes(timeFrame: TimeFrame, asset: Asset, option: AssetList, isUserTokenSelected: Boolean) {
        refreshQuotesUseCase(timeFrame, asset, option, isUserTokenSelected)
    }

    val state = quotesFlow
        .map {
            if (it.isErrorInitialLoading) {
                TerminalScreenState.Error
            } else {
                if (!it.isLoading) {
                    TerminalScreenState.Content(
                        barList = it.barList,
                        timeFrame = it.timeFrame,
                        selectedOption = it.selectedOption,
                        selectedAsset = it.selectedAsset,
                        isUserTokenSelected = it.isUserTokenSelected
                    ) as TerminalScreenState
                } else {
                    TerminalScreenState.Loading
                }
            }


        }
        .onStart {
            emit(TerminalScreenState.Loading)
        }

}