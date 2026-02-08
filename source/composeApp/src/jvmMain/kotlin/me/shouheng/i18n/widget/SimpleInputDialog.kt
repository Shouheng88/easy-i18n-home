package me.shouheng.i18n.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import easy_i18n.composeapp.generated.resources.Res
import easy_i18n.composeapp.generated.resources.text_cancel
import easy_i18n.composeapp.generated.resources.text_done
import easy_i18n.composeapp.generated.resources.text_sure
import org.jetbrains.compose.resources.stringResource

/** 输入对话框 */
@Composable
fun SimpleInputDialog(
    title: String,
    hint: String = "",
    tip: String = "",
    initialValue: String = "",
    singleLine: Boolean = true,
    numeric: Boolean = false,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var inputValue by remember { mutableStateOf(initialValue) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(tip, fontSize = 12.sp, modifier = Modifier.alpha(0.8f))
                if (numeric) {
                    OutlinedTextField(
                        value = inputValue,
                        onValueChange = {
                            val filtered = it.filter { char -> char.isDigit() }
                            inputValue = filtered
                        },
                        label = { Text(hint) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    )
                } else {
                    OutlinedTextField(
                        value = inputValue,
                        onValueChange = { inputValue = it },
                        label = { Text(hint) },
                        singleLine = singleLine,
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(inputValue) }) {
                Text(stringResource(Res.string.text_done))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(Res.string.text_cancel))
            }
        }
    )
}