package me.shouheng.i18n.view.i18n.dialog

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import easy_i18n.composeapp.generated.resources.Res
import easy_i18n.composeapp.generated.resources.set_i18n_translator_not_set
import easy_i18n.composeapp.generated.resources.text_begin
import easy_i18n.composeapp.generated.resources.text_cancel
import easy_i18n.composeapp.generated.resources.text_configured
import easy_i18n.composeapp.generated.resources.text_not_configured
import easy_i18n.composeapp.generated.resources.translate_auto
import easy_i18n.composeapp.generated.resources.translate_state_evaluated_time
import easy_i18n.composeapp.generated.resources.translate_state_source_lang
import easy_i18n.composeapp.generated.resources.translate_state_source_word
import easy_i18n.composeapp.generated.resources.translate_state_to_translate_count
import easy_i18n.composeapp.generated.resources.translate_translator
import easy_i18n.composeapp.generated.resources.translate_translator_not_configured
import me.shouheng.i18n.data.model.I18nLanguage
import me.shouheng.i18n.data.model.TranslatorType
import me.shouheng.i18n.manager.TranslatorManager
import me.shouheng.i18n.manager.showInfo
import me.shouheng.i18n.utils.StringUtils
import me.shouheng.i18n.vm.I18nViewModel
import me.shouheng.i18n.widget.NormalDialogFooter
import me.shouheng.i18n.widget.NormalDialogTitle
import org.jetbrains.compose.resources.stringResource

/** 翻译开始提示对话框 */
@Preview
@Composable
fun TranslateBeginDialog(
    vm: I18nViewModel,
    onClose: () -> Unit,
    onBegin: (I18nLanguage) -> Unit
) {
    val path = vm.pathContent.value
    val countNeedTranslate = vm.pathContent.value?.countWordsNeedTranslate() ?: 0
    var sourceLanguage by remember { mutableStateOf(path?.getSourceLanguage() ?: I18nLanguage.DEF) }
    Dialog(onDismissRequest = onClose) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .fillMaxWidth(0.9f)
            ) {
                // 标题
                NormalDialogTitle(stringResource(Res.string.translate_auto))
                // 翻译信息
                Column(
                    modifier = Modifier
                ) {
                    RowTranslatorType()
                    Row(
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 15.dp)
                    ) {
                        Text(stringResource(Res.string.translate_state_to_translate_count))
                        Spacer(modifier = Modifier.weight(1f))
                        Text("$countNeedTranslate", fontSize = 13.sp)
                    }
                    Row(
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 15.dp)
                    ) {
                        Text(stringResource(Res.string.translate_state_evaluated_time))
                        Spacer(modifier = Modifier.weight(1f))
                        Text(StringUtils.formatDuration(TranslatorManager.evaluateTimeCost(countNeedTranslate)), fontSize = 13.sp)
                    }
                    RowSourceLanguage(vm) {
                        sourceLanguage = it
                    }
                }
                // 底部操作区
                NormalDialogFooter(
                    left = stringResource(Res.string.text_cancel),
                    right = stringResource(Res.string.text_begin),
                    onLeft = onClose,
                    onRight = {
                        onBegin(sourceLanguage)
                    }
                )
            }
        }
    }
}

@Composable
private fun RowSourceLanguage(
    vm: I18nViewModel,
    onLanguageChanged: (I18nLanguage) -> Unit
) {
    val path = vm.pathContent.value
    val hasWordSourceLanguage = path?.hasWordSourceLanguage() == true
    var expandedLanguages by remember { mutableStateOf(false) }
    var sourceLanguage by remember { mutableStateOf(path?.getSourceLanguage() ?: I18nLanguage.DEF) }
    val languages = path?.getAllLanguages() ?: emptyList()
    Row(
        modifier = Modifier.clickable {
            expandedLanguages = true
        }.padding(vertical = 8.dp, horizontal = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(stringResource(Res.string.translate_state_source_lang))
        Spacer(modifier = Modifier.weight(1f))
        Box {
            Text(if (hasWordSourceLanguage) stringResource(Res.string.translate_state_source_word) else sourceLanguage.getReadableName(), fontSize = 13.sp)
            DropdownMenu(
                expanded = expandedLanguages,
                onDismissRequest = { expandedLanguages = false },
            ) {
                languages.forEach {
                    DropdownMenuItem(
                        text = {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(it.getReadableName(), fontSize = 15.sp)
                            }
                        },
                        onClick = {
                            sourceLanguage = it
                            onLanguageChanged(it)
                            vm.updateSourceLanguage(path, it)
                            expandedLanguages = false
                        }
                    )
                }
            }
        }
        if (!hasWordSourceLanguage) {
            Icon(
                Icons.Default.ArrowDropDown,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun RowTranslatorType() {
    var expandedTypes by remember { mutableStateOf(false) }
    var translatorType by remember { mutableStateOf(TranslatorManager.getTranslatorType()) }
    Row(
        modifier = Modifier.clickable {
            expandedTypes = true
        }.padding(vertical = 8.dp, horizontal = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(stringResource(Res.string.translate_translator))
        Spacer(modifier = Modifier.weight(1f))
        Box {
            Text(if (translatorType?.nameRes == null) stringResource(Res.string.set_i18n_translator_not_set) else stringResource(translatorType!!.nameRes), fontSize = 13.sp)
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
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(3.dp)
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
                            if (!it.isConfigured()) {
                                showInfo(Res.string.translate_translator_not_configured)
                                return@DropdownMenuItem
                            }
                            expandedTypes = false
                            TranslatorManager.setTranslatorType(it)
                            translatorType = it
                        }
                    )
                }
            }
        }
        Icon(
            Icons.Default.ArrowDropDown,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
        )
    }
}