package com.balex.stockforexcryptoquotes.di

import android.app.Application
import android.content.Context
import com.balex.stockforexcryptoquotes.data.datastore.Storage
import com.balex.stockforexcryptoquotes.presentation.ViewModelFactory

import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    modules = [
        DataModule::class,
        ViewModelModule::class
    ]
)
interface ApplicationComponent {

    fun getViewModelFactory(): ViewModelFactory


    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }

}


