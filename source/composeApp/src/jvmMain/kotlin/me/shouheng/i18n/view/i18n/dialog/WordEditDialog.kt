package me.shouheng.i18n.view.i18n.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import easy_i18n.composeapp.generated.resources.*
import io.github.vinceglb.filekit.absolutePath
import me.shouheng.i18n.data.Event
import me.shouheng.i18n.data.Router.ROUTE_SETTINGS
import me.shouheng.i18n.data.model.I18nDialogEditItem
import me.shouheng.i18n.data.model.I18nWordModel
import me.shouheng.i18n.data.model.SingleFileI18WordModel
import me.shouheng.i18n.manager.I18nManager
import me.shouheng.i18n.manager.TextManager
import me.shouheng.i18n.manager.TranslatorManager
import me.shouheng.i18n.manager.post
import me.shouheng.i18n.manager.showWarn
import me.shouheng.i18n.utils.FileUtils
import me.shouheng.i18n.view.i18n.TranslateStateView
import me.shouheng.i18n.vm.I18nViewModel
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/** 多语言词条编辑对话框 */
@Preview
@Composable
fun WordEditDialog(
    word: I18nWordModel,
    editItems: List<I18nDialogEditItem>,
    vm: I18nViewModel,
    onClose: () -> Unit,
) {
    var description by remember { mutableStateOf(word.description ?: "") }
    var anyThingChanged by remember { mutableStateOf(false) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var items by remember { mutableStateOf(editItems) }
    val wordUpdateState by vm.wordUpdateState.collectAsState()
    var showTypeMissDialog by remember { mutableStateOf(false) }
    var showTypeUnavailableDialog by remember { mutableStateOf(false) }
    val onDialogClose = {
        vm.clearWordTranslateState()
        onClose()
    }
    LaunchedEffect(Unit) {
        vm.wordUpdateState.collect {
            if (it?.isSuccess == true) {
                onDialogClose()
                vm.wordUpdateState.value = null
            }
        }
    }
    Dialog(onDismissRequest = onDialogClose) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight(0.8f)
                    .fillMaxWidth(0.9f)
            ) {
                // 标题
                Text(
                    word.name + word.getTypeDisplayName(),
                    style = TextStyle(fontSize = 18.sp),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(vertical = 15.dp, horizontal = 15.dp)
                        .fillMaxWidth(),
                )
                // 输入框
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 15.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // 编辑规则
                    if (word.isPossibleArray || word.isPossiblePlural) {
                        EditRuleView(word)
                    }
                    // 描述 编辑对话框
                    OutlinedTextField(
                        value = description,
                        textStyle = TextStyle(fontSize = 14.sp),
                        onValueChange = { value ->
                            description = value
                            anyThingChanged = true
                        },
                        label = { Text(stringResource(Res.string.word_desc)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    // 各语言录入规则
                    items.forEachIndexed { index, item ->
                        OutlinedTextField(
                            value = item.value,
                            textStyle = TextStyle(fontSize = 14.sp),
                            onValueChange = { value ->
                                items = items.toMutableList().also {
                                    val item = it[index]
                                    it[index] = item.copy(
                                        value = value,
                                        isError = (word.isPossibleArray && !TextManager.isArray(value))
                                                || (word.isPossiblePlural && !TextManager.isPlural(value))
                                    )
                                }
                                anyThingChanged = true
                            },
                            isError = item.isError,
                            label = { Text(item.hintGetter.get()) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    // 存储位置
                    if (word is SingleFileI18WordModel) {
                        Text(
                            "${stringResource(Res.string.word_save_path)}：${word.origin.file.absolutePath()}",
                            fontSize = 11.sp,
                            lineHeight = 13.sp,
                            modifier = Modifier.clickable {
                                FileUtils.openDirectory(word.path)
                            }
                        )
                    }
                }
                // 底部操作区
                BottomButtonsView(
                    vm,
                    enableSave = anyThingChanged,
                    onSave = {
                        // 更新词条
                        vm.updateWord(word, items, description)
                    },
                    onDelete = {
                        showDeleteConfirmDialog = true
                    },
                    onTranslate = {
                        val type = TranslatorManager.getTranslatorType()
                        if (type == null) {
                            showTypeMissDialog = true
                            return@BottomButtonsView
                        }
                        if (!type.isConfigured()) {
                            showTypeUnavailableDialog = true
                            return@BottomButtonsView
                        }
                        if (vm.isTranslatorRunning()) {
                            showWarn(Res.string.translate_auto_translating)
                            return@BottomButtonsView
                        }
                        vm.autoTranslate(description, items) {
                            items = it
                            anyThingChanged = true
                        }
                    },
                    onUpperFirstCase = {
                        val list = items.toMutableList()
                        list.forEachIndexed { index, item ->
                            list[index] = item.copy(
                                value = TextManager.upperFirstCase(item.value),
                            )
                        }
                        items = list
                        anyThingChanged = true
                    },
                    onClose = onDialogClose
                )
            }
        }
    }

    if (showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            title = { Text(stringResource(Res.string.word_delete_title)) },
            text = { Text(stringResource(Res.string.word_delete_message, word.name)) },
            confirmButton = {
                TextButton(onClick = {
                    vm.deleteWord(word)
                    showDeleteConfirmDialog = false
                    onDialogClose()
                }) {
                    Text(stringResource(Res.string.text_sure))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmDialog = false }) {
                    Text(stringResource(Res.string.text_cancel))
                }
            }
        )
    }
    if (wordUpdateState != null) {
        AlertDialog(
            onDismissRequest = { vm.wordUpdateState.value = null },
            title = { Text(stringResource(Res.string.word_update_failed)) },
            text = { Text(stringResource(Res.string.word_update_failed_with, word.name, "${wordUpdateState?.code}:${wordUpdateState?.message}")) },
            confirmButton = {
                TextButton(onClick = {
                    vm.wordUpdateState.value = null
                }) {
                    Text(stringResource(Res.string.text_got_it))
                }
            },
        )
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
}

/** 底部操作按钮 */
@Composable
private fun BottomButtonsView(
    vm: I18nViewModel,
    enableSave: Boolean,
    onSave: () -> Unit,
    onDelete: () -> Unit,
    onTranslate: () -> Unit,
    onUpperFirstCase: () -> Unit,
    onClose: () -> Unit,
) {
    val translateState by vm.wordTranslateState.collectAsState()
    var showMoreItems by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 15.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(enabled = enableSave, onClick = onSave) {
            Text(stringResource(Res.string.text_save), fontSize = 14.sp)
        }
        Box {
            Button(
                onClick = { showMoreItems = true },
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier
                    .defaultMinSize(1.dp, 1.dp) // 取消 minWidth/minHeight
                    .padding(0.dp),
            ) {
                Icon(Icons.Default.MoreVert, contentDescription = null)
            }
            DropdownMenu(
                expanded = showMoreItems,
                onDismissRequest = { showMoreItems = false }
            ) {
                DropdownMenuItem(
                    text = { Text(stringResource(Res.string.word_delete), fontSize = 14.sp, color = Color.Red) },
                    onClick = {
                        onDelete()
                        showMoreItems = false
                    }
                )
                DropdownMenuItem(
                    text = { Text(stringResource(Res.string.translate_auto), fontSize = 14.sp) },
                    onClick = {
                        onTranslate()
                        showMoreItems = false
                    }
                )
                DropdownMenuItem(
                    text = { Text(stringResource(Res.string.word_save_upper_first_case), fontSize = 14.sp) },
                    onClick = {
                        onUpperFirstCase()
                        showMoreItems = false
                    }
                )
            }
        }
        if (translateState != null) {
            TranslateStateView(translateState!!, simplified = true)
        }
        Spacer(Modifier.weight(1f))
        Button(onClick = onClose) {
            Text(stringResource(Res.string.text_cancel), fontSize = 14.sp, maxLines = 1)
        }
    }
}

/** 编辑规则的说明 */
@Composable
private fun EditRuleView(word: I18nWordModel) {
    var showPluralRuleDialog by remember { mutableStateOf(false) }
    var showArrayRuleDialog by remember { mutableStateOf(false) }
    if (word.isPossiblePlural) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.clickable {
                showPluralRuleDialog = true
            }.padding(3.dp)
        ) {
            Text(
                stringResource(Res.string.edit_rule_plural),
                fontSize = 11.sp,
                lineHeight = 13.sp,
            )
            Icon(
                Icons.Default.Info,
                contentDescription = null,
                modifier = Modifier.size(12.dp)
            )
        }
    }
    if (word.isPossibleArray) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.clickable {
                showArrayRuleDialog = true
            }.padding(3.dp)
        ) {
            Text(
                stringResource(Res.string.edit_rule_array),
                fontSize = 11.sp,
                lineHeight = 13.sp,
            )
            Icon(
                Icons.Default.Info,
                contentDescription = null,
                modifier = Modifier.size(12.dp)
            )
        }
    }
    if (showPluralRuleDialog) {
        AlertDialog(
            onDismissRequest = { showPluralRuleDialog = false },
            title = { Text(stringResource(Res.string.edit_rule_plural)) },
            text = { Text(I18nManager.getPluralEditRule()) },
            confirmButton = {
                TextButton(onClick = {
                    showPluralRuleDialog = false
                }) {
                    Text(stringResource(Res.string.text_got_it))
                }
            },
        )
    }
    if (showArrayRuleDialog) {
        AlertDialog(
            onDismissRequest = { showArrayRuleDialog = false },
            title = { Text(stringResource(Res.string.edit_rule_array)) },
            text = { Text(I18nManager.getArrayEditRule()) },
            confirmButton = {
                TextButton(onClick = {
                    showArrayRuleDialog = false
                }) {
                    Text(stringResource(Res.string.text_got_it))
                }
            },
        )
    }
}