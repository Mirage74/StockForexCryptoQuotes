package com.balex.stockforexcryptoquotes.data.model

import com.balex.stockforexcryptoquotes.data.datastore.Storage
import com.balex.stockforexcryptoquotes.domain.entity.Asset
import com.balex.stockforexcryptoquotes.domain.entity.AssetList
import com.balex.stockforexcryptoquotes.domain.entity.Bar
import com.balex.stockforexcryptoquotes.domain.entity.TimeFrame

data class CurrentAppState (
    val barList: List<Bar> = listOf(),
    val timeFrame: TimeFrame = TimeFrame.HOUR_1,
    val isLoading: Boolean = false,
    val isErrorInitialLoading: Boolean = false,
    val selectedOption: AssetList = AssetList.STOCKS,
    val selectedAsset: Asset = Asset.DEFAULT_STOCK,
    val isUserTokenSelected: Boolean = false,
    val userToken: String = Storage.NO_USER_TOKEN_IN_SHARED_PREFERENCES
)