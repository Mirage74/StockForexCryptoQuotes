package com.balex.stockforexcryptoquotes.presentation.main

import EncryptedDataStoreManager
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.balex.stockforexcryptoquotes.domain.entity.Asset
import com.balex.stockforexcryptoquotes.domain.entity.AssetList
import com.balex.stockforexcryptoquotes.domain.entity.TimeFrame
import com.balex.stockforexcryptoquotes.domain.usecases.GetQuotesUseCase
import com.balex.stockforexcryptoquotes.domain.usecases.RefreshQuotesUseCase
import com.balex.stockforexcryptoquotes.domain.usecases.SetUserToken
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject


class TerminalViewModel @Inject constructor(
    getQuotesUseCase: GetQuotesUseCase,
    private val refreshQuotesUseCase: RefreshQuotesUseCase,
    private val setUserToken: SetUserToken
) : ViewModel() {


    private val quotesFlow = getQuotesUseCase()


    fun refreshQuotes(timeFrame: TimeFrame, asset: Asset, option: AssetList, isUserTokenSelected: Boolean) {
        refreshQuotesUseCase(timeFrame, asset, option, isUserTokenSelected)
    }

    suspend fun getTokenFromDataStoreAndSetToRepository(context: Context) {
        viewModelScope.launch {
            val tokenFromDataStore = EncryptedDataStoreManager.getToken(context)?: ""
            Log.d("TerminalViewModel", tokenFromDataStore)
            if (tokenFromDataStore.isNotEmpty()) {

            }
        }
    }

    suspend fun saveTokenToDataStore(context: Context, token : String) {
        viewModelScope.launch {
            setUserToken(token)
            EncryptedDataStoreManager.saveToken(context, token)
        }
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