package com.balex.stockforexcryptoquotes.domain.usecases


import com.balex.stockforexcryptoquotes.data.model.QuotesAndFrame
import com.balex.stockforexcryptoquotes.domain.entity.TimeFrame
import com.balex.stockforexcryptoquotes.domain.repository.TerminalRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetQuotesUseCase @Inject constructor(
    private val repository: TerminalRepository
) {

    operator fun invoke(): StateFlow<QuotesAndFrame> {
        return repository.getQuotes()
    }
}