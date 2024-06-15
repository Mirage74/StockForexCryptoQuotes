package com.balex.stockforexcryptoquotes.data.network

import okhttp3.OkHttpClient
import com.balex.stockforexcryptoquotes.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.Locale

object ApiFactory {

    private const val KEY_PARAM = "key"
    private const val BASE_URL = "https://api.polygon.io/v2/"

//    private val okHttpClient = OkHttpClient.Builder()
//        .addInterceptor(HttpLoggingInterceptor().apply {
//            level = HttpLoggingInterceptor.Level.BODY
//        })
//        .build()



    private val okHttpClient = OkHttpClient.Builder()
        //.addInterceptor(interceptor)
        .addInterceptor { chain ->
            val originalRequest = chain.request()
            val newUrl = originalRequest
                .url
                .newBuilder()
                //.addQueryParameter(KEY_PARAM, BuildConfig.POLYGON_API_KEY)
                .build()
            val newRequest = originalRequest.newBuilder()
                .url(newUrl)
                .build()
            chain.proceed(newRequest)
        }.build()


    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        //.client(okHttpClient)
        .build()

    val apiService: ApiService = retrofit.create()
}