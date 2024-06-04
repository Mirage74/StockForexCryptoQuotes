package com.balex.stockforexcryptoquotes.data.repository

import com.balex.stockforexcryptoquotes.data.mapper.QuotesMapper
import com.balex.stockforexcryptoquotes.data.model.CurrentAppState
import com.balex.stockforexcryptoquotes.data.network.ApiService
import com.balex.stockforexcryptoquotes.domain.entity.Asset
import com.balex.stockforexcryptoquotes.domain.entity.AssetList
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject


class TerminalRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val mapper: QuotesMapper
) : TerminalRepository {

    private val defaultToken =  ApiService.API_TOKEN
    private var userToken =  NO_USER_TOKEN_SET


    private val coroutineScope = CoroutineScope(SupervisorJob())
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->

        _currentAppState = if (_currentAppStateLastState.barList.isEmpty()) {
            CurrentAppState(isErrorInitialLoading = true)
        } else {
            _currentAppStateLastState
        }
        coroutineScope.launch() {
            isCurrentAppStateNeedRefreshFlow.emit(Unit)
        }
    }


    private var _currentAppStateLastState = CurrentAppState()

    private var _currentAppState = CurrentAppState(isLoading = true)
    private val currentAppState: CurrentAppState
        get() = _currentAppState.copy()

    private val isCurrentAppStateNeedRefreshFlow = MutableSharedFlow<Unit>(replay = 1)


    override fun getQuotes(): StateFlow<CurrentAppState> = flow {
        coroutineScope.launch(exceptionHandler) {
            val newCurrentAppState = CurrentAppState(
                mapper.mapResponseToQuotes(apiService.loadBars(dateTo = getCurrentDate()).barList)
            )
            _currentAppState = newCurrentAppState
            isCurrentAppStateNeedRefreshFlow.emit(Unit)
        }
        isCurrentAppStateNeedRefreshFlow.collect {
            emit(currentAppState)
        }
    }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.Lazily,
            initialValue = currentAppState
        )

    override fun refreshQuotes(timeFrame: TimeFrame, asset: Asset, option: AssetList, isUserTokenSelected: Boolean) {
        _currentAppStateLastState = _currentAppState
        coroutineScope.launch(exceptionHandler) {
            _currentAppState = CurrentAppState(isLoading = true)
//            if (isUserTokenSelected) {
//                currentToken =
//            }
            isCurrentAppStateNeedRefreshFlow.emit(Unit)
            val newCurrentAppState = CurrentAppState(
                mapper.mapResponseToQuotes(
                    apiService.loadBars(
                        timeFrame = timeFrame.value,
                        asset_code = asset.symbol,
                        dateTo = getCurrentDate(),
                        apiToken = defaultToken
                    ).barList
                ),
                timeFrame,
                selectedOption = option,
                selectedAsset = asset,
                isUserTokenSelected = isUserTokenSelected
            )
            _currentAppState = newCurrentAppState
            isCurrentAppStateNeedRefreshFlow.emit(Unit)
        }
    }

    override fun setUserToken(token: String) {
        userToken = token
    }

    private fun getCurrentDate(): String {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return currentDate.format(formatter)
    }
companion object {
    const val NO_USER_TOKEN_SET = "NO_USER_TOKEN_SET"
}

}