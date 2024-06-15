package com.balex.stockforexcryptoquotes.data.network

import com.balex.stockforexcryptoquotes.data.model.ResultDto
import com.balex.stockforexcryptoquotes.domain.entity.Asset
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {


    @GET("aggs/ticker/{code}/range/{timeframe}/{dateFrom}/{dateTo}?adjusted=true")
    suspend fun loadBars(
        @Path("code") asset_code: String = Asset.DEFAULT_STOCK.symbol,
        @Path("timeframe") timeFrame: String = TIME_FRAME,
        @Path("dateFrom") dateFrom: String = DATE_FROM,
        @Path("dateTo") dateTo: String = DATE_TO,
        @Query("sort") sort: String = SORT,
        @Query("limit") limit: Int = LIMIT,
        @Query("apiKey") apiToken: String
    ): ResultDto

    companion object {
        const val TIME_FRAME = "1/hour"
        const val DATE_FROM = "2022-06-01"
        const val DATE_TO =  "2024-06-01"

        const val SORT = "desc"
        const val LIMIT = 50000
    }
}