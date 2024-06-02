package com.balex.stockforexcryptoquotes.domain.entity

data class ForexPairList(
    val forexPairList: List<Asset> = listOf(
        Asset("C:EURUSD", "Euro / USD"),
        Asset("C:USDJPY", "USD / Japanese yen"),
        Asset("C:GBPUSD", "Pound sterling / USD"),
        Asset("C:USDCHF", "USD / Swiss franc"),
        Asset("C:AUDUSD", "Australian dollar / USD"),
        Asset("C:USDCAD", "USD / Canadian dollar"),
        Asset("C:NZDUSD", "New Zealand dollar / USD")
    )
)
