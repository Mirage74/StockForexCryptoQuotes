package com.balex.stockforexcryptoquotes.presentation.main

import android.content.Context
import androidx.lifecycle.ViewModel
import com.balex.stockforexcryptoquotes.data.datastore.Storage
import com.balex.stockforexcryptoquotes.domain.entity.Asset
import com.balex.stockforexcryptoquotes.domain.entity.AssetList
import com.balex.stockforexcryptoquotes.domain.entity.TimeFrame
import com.balex.stockforexcryptoquotes.domain.usecases.ChangeRadioButtonSelectedUseCase
import com.balex.stockforexcryptoquotes.domain.usecases.GetQuotesUseCase
import com.balex.stockforexcryptoquotes.domain.usecases.RefreshQuotesUseCase
import com.balex.stockforexcryptoquotes.domain.usecases.SetUserToken
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject


class TerminalViewModel @Inject constructor(
    getQuotesUseCase: GetQuotesUseCase,
    private val refreshQuotesUseCase: RefreshQuotesUseCase,
    private val setUserToken: SetUserToken,
    private val changeRadioButtonSelectedUseCase: ChangeRadioButtonSelectedUseCase
) : ViewModel() {


    private val quotesFlow = getQuotesUseCase()


    fun refreshQuotes(timeFrame: TimeFrame, asset: Asset, option: AssetList, isUserTokenSelected: Boolean) {
        refreshQuotesUseCase(timeFrame, asset, option, isUserTokenSelected)
    }

    fun changeRadioButtonSelected() {
        changeRadioButtonSelectedUseCase()
    }

    fun getTokenFromStorageAndSetToRepository(context: Context) {
            val tokenFromStorage = Storage.getToken(context)
            //Log.d("TerminalViewModel", tokenFromStorage)
            if (tokenFromStorage != Storage.NO_USER_TOKEN_IN_SHARED_PREFERENCES) {
                setUserToken(tokenFromStorage)
            }
    }

    fun saveTokenToStorageAndRepository(context: Context, token : String) {
        Storage.saveToken(context, token)
        setUserToken(token)
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
                        isUserTokenSelected = it.isUserTokenSelected,
                        userToken = it.userToken
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