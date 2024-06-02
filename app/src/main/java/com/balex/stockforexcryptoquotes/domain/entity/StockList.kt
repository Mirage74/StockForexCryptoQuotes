package com.balex.stockforexcryptoquotes.domain.entity

data class StockList(
    val stockList: List<Asset> = listOf(
        Asset("AAPL", "Apple Inc."),
        Asset("BEKE", "KE Holdings Inc."),
        Asset("BIDU", "Baidu, Inc."),
        Asset("CPA", "Copa Holdings, S.A."),
        Asset("FYBR", "Frontier Communications Parent"),
        Asset("GOOGL", "Alphabet Inc."),
        Asset("HTHT", "H World Group Limited"),
        Asset("INSM", "Insmed, Inc."),
        Asset("IRDM", "Iridium Communications Inc."),
        Asset("LI", "Li Auto Inc."),
        Asset("LLYVA", "Liberty Media Corporation"),
        Asset("LNTH", "Lantheus Holdings, Inc"),
        Asset("MDGL", "Madrigal Pharmaceuticals"),
        Asset("MSFT", "Microsoft Corp"),
        Asset("ORCL", "Oracle Corp"),
        Asset("RARE", "Ultragenyx Pharmaceutical Inc."),
        Asset("RCKT", "Rocket Pharmaceuticals, Inc."),
        Asset("SHLS", "Shoals Technologies Group, Inc."),
        Asset("UBS", "UBS Group AG"),
        Asset("VAL", "Valaris Limited")
    )
)

