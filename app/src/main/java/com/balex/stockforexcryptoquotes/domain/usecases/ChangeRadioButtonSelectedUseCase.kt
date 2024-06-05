package com.balex.stockforexcryptoquotes.domain.usecases

import com.balex.stockforexcryptoquotes.domain.repository.TerminalRepository
import javax.inject.Inject

class ChangeRadioButtonSelectedUseCase @Inject constructor(
    private val repository: TerminalRepository
) {

    operator fun invoke() {
        return repository.changeRadioButtonSelected()
    }
}