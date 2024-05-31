package com.balex.stockforexcryptoquotes.presentation

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.balex.stockforexcryptoquotes.di.ApplicationComponent
import com.balex.stockforexcryptoquotes.di.DaggerApplicationComponent

class TerminalApplication : Application() {

    val component: ApplicationComponent by lazy {
        DaggerApplicationComponent
            .factory()
            .create(this)
    }
}

@Composable
fun getApplicationComponent(): ApplicationComponent {
    //Log.d("RECOMPOSITION_TAG", "getApplicationComponent")
    return (LocalContext.current.applicationContext as TerminalApplication).component
}