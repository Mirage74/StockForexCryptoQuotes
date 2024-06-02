package com.balex.stockforexcryptoquotes.data.model

import com.balex.stockforexcryptoquotes.domain.entity.Asset
import com.balex.stockforexcryptoquotes.domain.entity.AssetList
import com.balex.stockforexcryptoquotes.domain.entity.Bar
import com.balex.stockforexcryptoquotes.domain.entity.TimeFrame
import java.util.Collections

data class QuotesAndFrame (
    val barList: List<Bar> = Collections.emptyList(),
    val timeFrame: TimeFrame = TimeFrame.HOUR_1,
    val isLoading: Boolean = false,
    val isErrorInitialLoading: Boolean = false,
    val selectedOption: AssetList = AssetList.STOCKS,
    val selectedAsset: Asset = Asset.DEFAULT_STOCK
)