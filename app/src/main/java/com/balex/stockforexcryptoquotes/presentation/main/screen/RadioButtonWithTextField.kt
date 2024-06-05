package com.balex.stockforexcryptoquotes.presentation.main.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
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
import com.balex.stockforexcryptoquotes.data.datastore.Storage
import com.balex.stockforexcryptoquotes.presentation.main.TerminalChartState

@Composable
fun RadioButtonWithTextField(
    isUserTokenSelected: Boolean,
    currentToken: String,
    onSelectedButtonChanged: () -> Unit,
    onTokenSaved: (String) -> Unit
) {
    var isRadioButtonUserTokenChecked by remember { mutableStateOf(isUserTokenSelected) }
    var isShowUserTokenField by remember(isRadioButtonUserTokenChecked.hashCode()) {
        mutableStateOf(
            true
        )
    }
    var textFieldValue by remember { mutableStateOf(currentToken) }

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
                    if (!isRadioButtonUserTokenChecked) {
                        isRadioButtonUserTokenChecked = true
                        onSelectedButtonChanged()
                        isShowUserTokenField = true
                    }
                    isShowUserTokenField = !isShowUserTokenField
                }
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                modifier = Modifier
                    .clickable {
                        if (!isRadioButtonUserTokenChecked) {
                            isRadioButtonUserTokenChecked = true
                            onSelectedButtonChanged()
                            isShowUserTokenField = true
                        }
                        isShowUserTokenField = !isShowUserTokenField
                    },
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
                    if (isRadioButtonUserTokenChecked) {
                        isRadioButtonUserTokenChecked = false
                        onSelectedButtonChanged()
                    }

                }
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                modifier = Modifier
                    .clickable {
                        if (isRadioButtonUserTokenChecked) {
                            isRadioButtonUserTokenChecked = false
                            onSelectedButtonChanged()
                        }
                    },
                text = "use default token",
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (isRadioButtonUserTokenChecked && isShowUserTokenField) {
            Row (
                modifier = Modifier
                .imePadding()
            ) {
                if ((textFieldValue == Storage.NO_USER_TOKEN_IN_SHARED_PREFERENCES) || (textFieldValue == NO_USER_TOKEN_SET)) {
                    textFieldValue = ""
                }
                TextField(
                    value = textFieldValue,
                    onValueChange = { textFieldValue = it },
                    label = { Text("enter token:") },
                    modifier = Modifier
                        .weight(3f)
                )
                Button(
                    modifier = Modifier
                        .weight(1f),
                    onClick = {
                        isShowUserTokenField = false
                        onTokenSaved(textFieldValue)
                    }
                )
                {
                    Text(text = "SAVE")
                }
            }
        }

    }
}

const val NO_USER_TOKEN_SET = "NO_USER_TOKEN_SET"

