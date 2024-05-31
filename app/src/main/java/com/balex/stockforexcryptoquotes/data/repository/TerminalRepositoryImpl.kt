package com.balex.stockforexcryptoquotes.data.repository

import com.balex.stockforexcryptoquotes.data.mapper.QuotesMapper
import com.balex.stockforexcryptoquotes.data.model.QuotesAndFrame
import com.balex.stockforexcryptoquotes.data.network.ApiService
import com.balex.stockforexcryptoquotes.domain.entity.TimeFrame
import com.balex.stockforexcryptoquotes.domain.repository.TerminalRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


class TerminalRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val mapper: QuotesMapper
): TerminalRepository {

    private val coroutineScope = CoroutineScope(SupervisorJob())
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->

        if (_quotesListAndFrameLastState.barList.isEmpty()) {
            _quotesListAndFrame = QuotesAndFrame(isErrorInitialLoading = true)
        } else {
            _quotesListAndFrame = _quotesListAndFrameLastState
        }
        //isQuotesListNeedRefreshFlow.tryEmit(Unit)
            coroutineScope.launch() {
                isQuotesListNeedRefreshFlow.emit(Unit)
            }
    }


    private var _quotesListAndFrameLastState = QuotesAndFrame()

    private var _quotesListAndFrame = QuotesAndFrame(isLoading = true)
    private val quotesListAndFrame: QuotesAndFrame
        get() = QuotesAndFrame(
            _quotesListAndFrame.barList.toList(),
            _quotesListAndFrame.timeFrame,
            _quotesListAndFrame.isLoading,
            _quotesListAndFrame.isErrorInitialLoading
        )

    private val isQuotesListNeedRefreshFlow = MutableSharedFlow<Unit>(replay = 1)


    override fun getQuotes(): StateFlow<QuotesAndFrame> = flow {
        coroutineScope.launch (exceptionHandler) {
            val newQuotesAndFrame = QuotesAndFrame(mapper.mapResponseToQuotes(apiService.loadBars().barList), TIME_FRAME_DEFAULT)
            _quotesListAndFrame = newQuotesAndFrame
            isQuotesListNeedRefreshFlow.emit(Unit)
        }
        isQuotesListNeedRefreshFlow.collect {
            emit(quotesListAndFrame)
        }
    }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.Lazily,
            initialValue = quotesListAndFrame
        )

    override fun refreshQuotes(timeFrame: TimeFrame){
        _quotesListAndFrameLastState = _quotesListAndFrame
        coroutineScope.launch (exceptionHandler) {
            _quotesListAndFrame = QuotesAndFrame(isLoading = true)
            isQuotesListNeedRefreshFlow.emit(Unit)
            val newQuotesAndFrame = QuotesAndFrame(
                mapper.mapResponseToQuotes(apiService.loadBars(timeFrame = timeFrame.value).barList),
                timeFrame
            )
            _quotesListAndFrame = newQuotesAndFrame
            isQuotesListNeedRefreshFlow.emit(Unit)
        }
    }

    companion object {
        private val TIME_FRAME_DEFAULT = TimeFrame.HOUR_1
    }

}