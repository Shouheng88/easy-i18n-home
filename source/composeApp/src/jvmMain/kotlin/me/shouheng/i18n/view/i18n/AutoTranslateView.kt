package me.shouheng.i18n.view.i18n

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import easy_i18n.composeapp.generated.resources.*
import me.shouheng.i18n.data.Event
import me.shouheng.i18n.data.Router.ROUTE_SETTINGS
import me.shouheng.i18n.manager.TranslatorManager
import me.shouheng.i18n.manager.post
import me.shouheng.i18n.manager.showWarn
import me.shouheng.i18n.view.i18n.dialog.TranslateBeginDialog
import me.shouheng.i18n.vm.I18nViewModel
import me.shouheng.i18n.widget.SmallIconButton
import org.jetbrains.compose.resources.stringResource

/** 自动翻译按钮及其逻辑 */
@Preview
@Composable
fun AutoTranslateView(vm: I18nViewModel) {
    var showTypeMissDialog by remember { mutableStateOf(false) }
    var showTypeUnavailableDialog by remember { mutableStateOf(false) }
    var showTranslateBeginDialog by remember { mutableStateOf(false) }
    SmallIconButton(Icons.Filled.PlayArrow) {
        val type = TranslatorManager.getTranslatorType()
        if (type == null) {
            showTypeMissDialog = true
            return@SmallIconButton
        }
        if (!type.isConfigured()) {
            showTypeUnavailableDialog = true
            return@SmallIconButton
        }
        if (vm.isTranslatorRunning()) {
            showWarn(Res.string.translate_auto_translating)
            return@SmallIconButton
        }
        showTranslateBeginDialog = true
    }
    if (showTypeMissDialog) {
        AlertDialog(
            onDismissRequest = { showTypeMissDialog = false },
            title = { Text(stringResource(Res.string.translate_auto)) },
            text = { Text(stringResource(Res.string.translate_type_set_message)) },
            confirmButton = {
                TextButton(onClick = {
                    showTypeMissDialog = false
                    post(Event.EVENT_NAME_TO_PAGE, ROUTE_SETTINGS)
                }) {
                    Text(stringResource(Res.string.text_to_set))
                }
            },
            dismissButton = {
                TextButton(onClick = { showTypeMissDialog = false }) {
                    Text(stringResource(Res.string.text_cancel))
                }
            }
        )
    }
    if (showTypeUnavailableDialog) {
        AlertDialog(
            onDismissRequest = { showTypeUnavailableDialog = false },
            title = { Text(stringResource(Res.string.translate_auto)) },
            text = { Text(stringResource(Res.string.translate_translator_info_set_message)) },
            confirmButton = {
                TextButton(onClick = {
                    showTypeUnavailableDialog = false
                    post(Event.EVENT_NAME_TO_PAGE, ROUTE_SETTINGS)
                }) {
                    Text(stringResource(Res.string.text_to_set))
                }
            },
            dismissButton = {
                TextButton(onClick = { showTypeUnavailableDialog = false }) {
                    Text(stringResource(Res.string.text_cancel))
                }
            }
        )
    }
    if (showTranslateBeginDialog) {
        TranslateBeginDialog(
            vm,
            onClose = { showTranslateBeginDialog = false },
            onBegin = { language ->
                vm.autoTranslate(language)
                showTranslateBeginDialog = false
            }
        )
    }
}