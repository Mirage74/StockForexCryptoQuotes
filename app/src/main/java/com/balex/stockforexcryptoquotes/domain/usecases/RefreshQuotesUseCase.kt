package com.balex.stockforexcryptoquotes.domain.usecases

import com.balex.stockforexcryptoquotes.domain.entity.Asset
import com.balex.stockforexcryptoquotes.domain.entity.AssetList
import com.balex.stockforexcryptoquotes.domain.entity.TimeFrame
import com.balex.stockforexcryptoquotes.domain.repository.TerminalRepository
import javax.inject.Inject

class RefreshQuotesUseCase @Inject constructor(
    private val repository: TerminalRepository
) {

    operator fun invoke(timeFrame: TimeFrame, asset: Asset, option: AssetList, isUserTokenSelected: Boolean) {
        return repository.refreshQuotes(timeFrame, asset, option, isUserTokenSelected)
    }
}