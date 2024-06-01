package com.balex.stockforexcryptoquotes.domain.entity

data class CryptoList(
    val cryptoList: List<Asset> = listOf(
        Asset("X:BTCUSD", "Bitcoin - United States Dollar"),
        Asset("X:ETHUSD", "Ethereum - United States Dollar"),
        Asset("X:SOLUSD", "Solana - United States Dollar"),
        Asset("X:XRPUSD", "Ripple - United States Dollar"),
        Asset("X:DOGEUSD", "Dogecoin - United States Dollar"),
        Asset("X:ADAUSD", "Cardano - United States Dollar"),
        Asset("X:SHIBUSD", "Shiba Inu - United States Dollar"),
        Asset("X:AVAXUSD", "Avalanche - United States Dollar")
    )
)
