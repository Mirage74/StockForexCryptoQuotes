package com.balex.stockforexcryptoquotes.presentation.main

import androidx.lifecycle.ViewModel
import com.balex.stockforexcryptoquotes.domain.entity.TimeFrame
import com.balex.stockforexcryptoquotes.domain.usecases.GetQuotesUseCase
import com.balex.stockforexcryptoquotes.domain.usecases.RefreshQuotesUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class TerminalViewModel @Inject constructor(
    getQuotesUseCase: GetQuotesUseCase,
    private val refreshQuotesUseCase: RefreshQuotesUseCase
) : ViewModel() {

    private val quotesFlow = getQuotesUseCase()

    fun refreshQuotes(timeFrame: TimeFrame) {
        refreshQuotesUseCase(timeFrame)
    }


    val state = quotesFlow
        //.filter { it.barList.isNotEmpty() }
        .map {
            if (it.isErrorInitialLoading) {
                TerminalScreenState.Error
            } else {
                if (!it.isLoading) {
                    TerminalScreenState.Content(
                        barList = it.barList,
                        timeFrame = it.timeFrame
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