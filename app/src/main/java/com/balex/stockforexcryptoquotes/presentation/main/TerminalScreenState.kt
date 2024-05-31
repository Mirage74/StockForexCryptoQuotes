package com.balex.stockforexcryptoquotes.presentation.main


import com.balex.stockforexcryptoquotes.domain.entity.Bar
import com.balex.stockforexcryptoquotes.domain.entity.TimeFrame

sealed class TerminalScreenState {

    data object Initial : TerminalScreenState()

    data object Loading : TerminalScreenState()

    data object Error : TerminalScreenState()

    data class Content(val barList: List<Bar>, val timeFrame: TimeFrame) : TerminalScreenState()

}