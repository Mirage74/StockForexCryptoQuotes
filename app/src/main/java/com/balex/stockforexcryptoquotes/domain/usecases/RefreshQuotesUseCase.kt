package com.balex.stockforexcryptoquotes.domain.usecases

import com.balex.stockforexcryptoquotes.domain.entity.TimeFrame
import com.balex.stockforexcryptoquotes.domain.repository.TerminalRepository
import javax.inject.Inject

class RefreshQuotesUseCase @Inject constructor(
    private val repository: TerminalRepository
) {

    operator fun invoke(timeFrame: TimeFrame) {
        return repository.refreshQuotes(timeFrame)
    }
}