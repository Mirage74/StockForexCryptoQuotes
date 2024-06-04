package com.balex.stockforexcryptoquotes.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import com.balex.stockforexcryptoquotes.presentation.main.screen.Terminal


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Terminal(LocalContext.current)
        }
    }
}

