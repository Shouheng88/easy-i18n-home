package me.shouheng.i18n.view.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import easy_i18n.composeapp.generated.resources.*
import me.shouheng.i18n.data.UIConst
import me.shouheng.i18n.data.model.TranslatorType
import me.shouheng.i18n.manager.TranslatorManager
import me.shouheng.i18n.manager.showError
import me.shouheng.i18n.manager.showSuccess
import me.shouheng.i18n.view.settings.dialog.TranslatorSettingDialog
import me.shouheng.i18n.widget.SettingItemView
import me.shouheng.i18n.widget.SettingSectionCard
import me.shouheng.i18n.widget.SettingSwitchItemView
import me.shouheng.i18n.widget.SimpleInputDialog
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun I18nSettingCard() {
    SettingSectionCard(
        stringResource(Res.string.set_i18n),
        Modifier.fillMaxWidth(UIConst.SETTING_ITEM_MAX_FRACTION)
    ) {
        SettingAPIITem()
        SettingTranslatorItem()
        SettingDelayItem()
        SettingTabSpaceItem()
        SettingImprovementItem()
    }
}

@Composable
private fun SettingImprovementItem() {
    var enabled by remember { mutableStateOf(TranslatorManager.isTranslatorImprovementEnabled()) }
    SettingSwitchItemView(
        Icons.Default.Stars,
        stringResource(Res.string.set_i18n_improve_title),
        stringResource(Res.string.set_i18n_improve_message),
        enabled,
        onChanged = {
            enabled = it
            TranslatorManager.setTranslatorImprovement(it)
        }
    )
}

/** XML 文件写入时缩进空格数 */
@Composable
private fun SettingTabSpaceItem() {
    var isShowTabSpaceCountSelector by remember { mutableStateOf(false) }
    var tabSpaceCount by remember { mutableStateOf(TranslatorManager.getXMLTabSpaceCount()) }
    Box {
        SettingItemView(
            Icons.Default.SpaceBar,
            stringResource(Res.string.set_i18n_xml_tab_title),
            stringResource(Res.string.set_i18n_xml_tab_message),
            "$tabSpaceCount",
            onClick = { isShowTabSpaceCountSelector = true }
        )
        DropdownMenu(
            expanded = isShowTabSpaceCountSelector,
            onDismissRequest = { isShowTabSpaceCountSelector = false },
        ) {
            DropdownMenuItem(
                text = {
                    Text(stringResource(Res.string.set_i18n_xml_tab_2), fontSize = 15.sp, modifier = Modifier.padding(vertical = 8.dp))
                },
                onClick = {
                    tabSpaceCount = 2
                    TranslatorManager.setXMLTabSpaceCount(2)
                }
            )
            DropdownMenuItem(
                text = {
                    Text(stringResource(Res.string.set_i18n_xml_tab_4), fontSize = 15.sp, modifier = Modifier.padding(vertical = 8.dp))
                },
                onClick = {
                    tabSpaceCount = 4
                    TranslatorManager.setXMLTabSpaceCount(4)
                }
            )
        }
    }
}

/** 翻译的延时时间 */
@Composable
private fun SettingDelayItem() {
    var showDelayEditDialog by remember { mutableStateOf(false) }
    var delay by remember { mutableStateOf(TranslatorManager.getTranslateDelay()) }
    SettingItemView(
        Icons.Default.Schedule,
        stringResource(Res.string.set_i18n_delay_title),
        stringResource(Res.string.set_i18n_delay_message),
        "%.1fs".format(delay * 1f / 1000),
        onClick = {
            showDelayEditDialog = true
        }
    )
    if (showDelayEditDialog) {
        SimpleInputDialog(
            stringResource(Res.string.set_i18n_delay_input_title),
            stringResource(Res.string.set_i18n_delay_input_message),
            initialValue = "$delay",
            singleLine = false,
            numeric = true,
            onConfirm = {
                val newDelay = it.toLongOrNull()
                if (newDelay == null) {
                    showError(Res.string.set_i18n_delay_input_illegal)
                    return@SimpleInputDialog
                }
                showSuccess(Res.string.text_update_succeed)
                delay = newDelay
                showDelayEditDialog = false
            },
            onDismiss = {
                showDelayEditDialog = false
            }
        )
    }
}

/** 翻译器设置条目 */
@Composable
private fun SettingTranslatorItem() {
    var showTranslatorSetDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    SettingItemView(
        Icons.Default.Api,
        stringResource(Res.string.set_i18n_api_title),
        stringResource(Res.string.set_i18n_api_message),
        "",
        onClick = {
            showTranslatorSetDialog = true
        }
    )
    if (showTranslatorSetDialog) {
        TranslatorSettingDialog {
            showTranslatorSetDialog = false
        }
    }
}

/** 翻译器类型设置条目 */
@Composable
private fun SettingAPIITem() {
    var expandedTypes by remember { mutableStateOf(false) }
    var translatorType by remember { mutableStateOf(TranslatorManager.getTranslatorType()) }
    var showInfoSetDialog by remember { mutableStateOf(false) }
    var showTranslatorSetDialog by remember { mutableStateOf(false) }
    var showAiSettingDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Box {
        SettingItemView(
            Icons.Default.Translate,
            stringResource(Res.string.set_i18n_translator_type_title),
            stringResource(Res.string.set_i18n_translator_type_message),
            stringResource(if (translatorType?.nameRes == null) Res.string.set_i18n_translator_not_set else translatorType!!.nameRes),
            onClick = {
                expandedTypes = true
            }
        )
        DropdownMenu(
            expanded = expandedTypes,
            onDismissRequest = { expandedTypes = false },
        ) {
            TranslatorType.all.forEach {
                DropdownMenuItem(
                    text = {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column (
                                verticalArrangement = Arrangement.spacedBy(0.dp)
                            ) {
                                Text(stringResource(it.nameRes), fontSize = 15.sp)
                                val configured = it.isConfigured()
                                Text(
                                    stringResource(if (configured) Res.string.text_configured else Res.string.text_not_configured),
                                    fontSize = 12.sp,
                                    color = if (configured) MaterialTheme.colorScheme.primary else Color.Red,
                                )
                            }
                        }
                    },
                    onClick = {
                        expandedTypes = false
                        TranslatorManager.setTranslatorType(it)
                        translatorType = it
                        val isConfigured = it.isConfigured()
                        if (!isConfigured) {
                            showInfoSetDialog = true
                        } else {
                            showSuccess(Res.string.set_i18n_translator_type_set_succeed)
                        }
                    }
                )
            }
        }
    }

    // 引导完善 API 信息对话框
    if (showInfoSetDialog) {
        AlertDialog(
            onDismissRequest = { showInfoSetDialog = false },
            title = { Text(stringResource(Res.string.text_tips)) },
            text = { Text(stringResource(Res.string.set_i18n_translator_not_configured)) },
            confirmButton = {
                TextButton(onClick = {
                    showInfoSetDialog = false
                    showTranslatorSetDialog = true
                }) {
                    Text(stringResource(Res.string.text_to_set))
                }
            },
            dismissButton = {
                TextButton(onClick = { showInfoSetDialog = false }) {
                    Text(stringResource(Res.string.text_cancel))
                }
            }
        )
    }

    // API 设置对话框
    if (showTranslatorSetDialog) {
        TranslatorSettingDialog {
            showTranslatorSetDialog = false
        }
    }
}

@Preview
@Composable
private fun I18nSettingCardPreview() {
    I18nSettingCard()
}