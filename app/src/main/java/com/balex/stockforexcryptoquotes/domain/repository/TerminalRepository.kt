package com.balex.stockforexcryptoquotes.domain.repository

import com.balex.stockforexcryptoquotes.data.model.QuotesAndFrame
import com.balex.stockforexcryptoquotes.domain.entity.Asset
import com.balex.stockforexcryptoquotes.domain.entity.AssetList
import com.balex.stockforexcryptoquotes.domain.entity.TimeFrame
import kotlinx.coroutines.flow.StateFlow

interface TerminalRepository {
    fun getQuotes(): StateFlow<QuotesAndFrame>

    fun refreshQuotes(timeFrame: TimeFrame, asset: Asset, option: AssetList)
}