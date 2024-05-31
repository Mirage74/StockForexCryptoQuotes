package com.balex.stockforexcryptoquotes.di

import com.balex.stockforexcryptoquotes.data.network.ApiFactory
import com.balex.stockforexcryptoquotes.data.network.ApiService
import com.balex.stockforexcryptoquotes.data.repository.TerminalRepositoryImpl
import com.balex.stockforexcryptoquotes.domain.repository.TerminalRepository
import dagger.Binds
import dagger.Module
import dagger.Provides


@Module
interface DataModule {

    @ApplicationScope
    @Binds
    fun bindRepository(impl: TerminalRepositoryImpl): TerminalRepository


    companion object {
        @ApplicationScope
        @Provides
        fun provideApiService(): ApiService {
            return ApiFactory.apiService
        }
    }
}