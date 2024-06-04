package com.balex.stockforexcryptoquotes.presentation.main.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.balex.stockforexcryptoquotes.presentation.main.TerminalChartState

@Composable
fun RadioButtonWithTextField(
    isUserTokenSelected: Boolean,
    onTerminalRadioButtonStateChanged: () -> Unit
) {
    var isRadioButtonUserTokenChecked by remember { mutableStateOf(isUserTokenSelected) }
    var textFieldValue by remember { mutableStateOf("") }

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color.Yellow,
                    unselectedColor = Color.Green
                ),
                selected = isRadioButtonUserTokenChecked,
                onClick = {
                    isRadioButtonUserTokenChecked = !isRadioButtonUserTokenChecked
                    onTerminalRadioButtonStateChanged()
                }
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "use user's token",
                color = Color.White
            )

            RadioButton(
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color.Yellow,
                    unselectedColor = Color.Green
                ),
                selected = !isRadioButtonUserTokenChecked,
                onClick = {
                    isRadioButtonUserTokenChecked = !isRadioButtonUserTokenChecked
                }
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "use default token",
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (isRadioButtonUserTokenChecked) {
            Row {
                TextField(
                    value = textFieldValue,
                    onValueChange = { textFieldValue = it },
                    label = { Text("Enter token here") },
                    modifier = Modifier.weight(4f)
                )
                Button(
                    modifier = Modifier
                        .weight(1f),
                    onClick = { onTerminalRadioButtonStateChanged() }
                )
                {
                    Text(text = "SAVE")
                }
            }
        }

    }
}
