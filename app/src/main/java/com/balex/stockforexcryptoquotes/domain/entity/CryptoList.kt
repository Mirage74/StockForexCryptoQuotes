package com.balex.stockforexcryptoquotes.domain.entity

data class CryptoList(
    val cryptoList: List<Asset> = listOf(
        Asset("X:BTCUSD", "Bitcoin / USD"),
        Asset("X:ETHUSD", "Ethereum / USD"),
        Asset("X:SOLUSD", "Solana / USD"),
        Asset("X:XRPUSD", "Ripple / USD"),
        Asset("X:DOGEUSD", "Dogecoin / USD"),
        Asset("X:ADAUSD", "Cardano / USD"),
        Asset("X:SHIBUSD", "Shiba Inu / USD"),
        Asset("X:AVAXUSD", "Avalanche / USD")
    )
)
