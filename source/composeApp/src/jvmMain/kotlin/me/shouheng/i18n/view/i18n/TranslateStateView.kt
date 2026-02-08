package me.shouheng.i18n.view.i18n

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ripple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import easy_i18n.composeapp.generated.resources.*
import me.shouheng.i18n.data.Colors
import me.shouheng.i18n.data.model.AutoTranslateState
import me.shouheng.i18n.utils.extension.alpha
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/** 自动翻译的状态展示控件 */
@Preview
@Composable
fun TranslateStateView(state: AutoTranslateState, simplified: Boolean = false) {
    var showStateDialog by remember { mutableStateOf(false) }
    when (state.status) {
        AutoTranslateState.Status.RUNNING -> {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Colors.warn.alpha(0.1f))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(bounded = true),
                        onClick = { showStateDialog = true }
                    )
                    .padding(vertical = 4.dp, horizontal = 10.dp)
            ) {
                Text(
                    if (simplified) state.getPercentageText() else
                        "${stringResource(Res.string.translate_state_translating)} ${state.getPercentageText()}",
                    fontSize = 14.sp,
                    color = Colors.warn
                )
                Icon(Icons.Default.Sync,
                    contentDescription = null,
                    tint = Colors.warn,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
        AutoTranslateState.Status.COMPLETED -> {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Colors.success.alpha(0.1f))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(bounded = true),
                        onClick = { showStateDialog = true }
                    )
                    .padding(vertical = 4.dp, horizontal = 10.dp)
            ) {
                Text(stringResource(Res.string.translate_state_completed), fontSize = 14.sp, color = Colors.success)
                Icon(Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Colors.success,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
        AutoTranslateState.Status.ERROR -> {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Colors.error.alpha(0.1f))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(bounded = true),
                        onClick = { showStateDialog = true }
                    )
                    .padding(vertical = 4.dp, horizontal = 10.dp)
            ) {
                Text(stringResource(Res.string.translate_state_error), fontSize = 14.sp, color = Colors.error)
                Icon(Icons.Default.Error,
                    contentDescription = null,
                    tint = Colors.error,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
    if (showStateDialog) {
        AlertDialog(
            onDismissRequest = { showStateDialog = false },
            title = { Text(stringResource(Res.string.translate_state)) },
            text = {
                Column {
                    TranslateStateRowView(
                        stringResource(Res.string.translate_state_state),
                        state.status.getDisplayName(),
                        valueColor = state.status.getDisplayColor()
                    )
                    if (state.errorCode != null) {
                        TranslateStateRowView(
                            stringResource(Res.string.translate_state_error_name),
                            "${state.errorCode}:${state.errorMessage}",
                            valueColor = Colors.error
                        )
                    }
                    if (state.translatedCount != 0) {
                        TranslateStateRowView(stringResource(Res.string.translate_state_translated_count), "${state.translatedCount}")
                    }
                    if (state.errorCount != 0) {
                        TranslateStateRowView(stringResource(Res.string.translate_state_error_count), "${state.errorCount}")
                    }
                    if (state.skippedCount != 0) {
                        TranslateStateRowView(stringResource(Res.string.translate_state_skipped_count), "${state.skippedCount}")
                    }
                    if (state.requestCount != 0) {
                        TranslateStateRowView(stringResource(Res.string.translate_state_request_count), "${state.requestCount}")
                    }
                    TranslateStateRowView(stringResource(Res.string.translate_state_count), "${state.total}")
                }
            },
            confirmButton = {
                TextButton(onClick = { showStateDialog = false }) {
                    Text("确定")
                }
            }
        )
    }
}

/** 状态对话框列表条目 */
@Composable
private fun TranslateStateRowView(
    name: String,
    value: String,
    valueColor: Color = Color.Unspecified
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(name, textAlign = TextAlign.End, modifier = Modifier.weight(1f))
        Text(value, modifier = Modifier.weight(1f), color = valueColor)
    }
}