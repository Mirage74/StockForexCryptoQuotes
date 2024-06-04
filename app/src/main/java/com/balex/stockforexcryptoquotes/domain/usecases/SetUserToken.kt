package com.balex.stockforexcryptoquotes.domain.usecases

import com.balex.stockforexcryptoquotes.domain.repository.TerminalRepository
import javax.inject.Inject

class SetUserToken @Inject constructor(
    private val repository: TerminalRepository
) {

    operator fun invoke(token: String) {
        return repository.setUserToken(token)
    }
}