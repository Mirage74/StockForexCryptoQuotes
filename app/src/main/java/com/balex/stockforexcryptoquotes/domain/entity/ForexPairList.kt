package com.balex.stockforexcryptoquotes.domain.entity

data class ForexPairList(
    val forexPairList: List<Asset> = listOf(
        Asset("C:EURUSD", "Euro - United States Dollar"),
        Asset("C:USDJPY", "United States dollar - Japanese yen"),
        Asset("C:GBPUSD", "Pound sterling - United States Dollar"),
        Asset("C:USDCHF", "United States dollar - Swiss franc"),
        Asset("C:AUDUSD", "Australian dollar - United States Dollar"),
        Asset("C:USDCAD", "United States dollar - Canadian dollar"),
        Asset("C:NZDUSD", "New Zealand dollar - United States Dollar")
    )
)
