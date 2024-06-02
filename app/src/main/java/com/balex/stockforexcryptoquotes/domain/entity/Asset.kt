package com.balex.stockforexcryptoquotes.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Asset(
    val symbol: String,
    val description: String
): Parcelable {
    companion object {
        val DEFAULT_STOCK = Asset("AAPL", "Apple Inc.")
        val DEFAULT_FOREX = Asset("C:EURUSD", "Euro / USD")
        val DEFAULT_CRYPTO = Asset("X:BTCUSD", "Bitcoin / USD")
    }
}
