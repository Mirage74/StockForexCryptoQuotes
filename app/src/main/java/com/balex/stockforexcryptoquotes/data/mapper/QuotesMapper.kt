package com.balex.stockforexcryptoquotes.data.mapper

import com.balex.stockforexcryptoquotes.data.model.BarDto
import com.balex.stockforexcryptoquotes.domain.entity.Bar
import javax.inject.Inject

class QuotesMapper @Inject constructor() {

    fun mapResponseToQuotes(responseDto: List<BarDto>): List<Bar> {
        val result = mutableListOf<Bar>()


        val nonEmptyQuotes = responseDto.stream().filter {
                e -> (!e.open.isNaN() && !e.close.isNaN() && !e.high.isNaN() && !e.low.isNaN() && e.time >  0)
        }
        for (quote in nonEmptyQuotes) {

            val bar = Bar(
                open = quote.open,
                close = quote.close,
                high = quote.high,
                low = quote.low,
                calendar = quote.calendar
            )
            result.add(bar)
        }
        return result
    }
}