package com.balex.stockforexcryptoquotes.presentation.main.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RadioButtonWithTextField() {
    var isRadioButtonChecked by remember { mutableStateOf(false) }
    var textFieldValue by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = isRadioButtonChecked,
                onClick = { isRadioButtonChecked = !isRadioButtonChecked }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Show TextField",
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (isRadioButtonChecked) {
            TextField(

                value = textFieldValue,
                onValueChange = { textFieldValue = it },
                label = { Text("Enter text") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}