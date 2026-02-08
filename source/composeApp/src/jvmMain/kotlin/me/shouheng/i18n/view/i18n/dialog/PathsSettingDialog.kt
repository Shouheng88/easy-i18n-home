package me.shouheng.i18n.view.i18n.dialog

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import easy_i18n.composeapp.generated.resources.*
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.openDirectoryPicker
import kotlinx.coroutines.launch
import me.shouheng.i18n.data.model.PathDetected
import me.shouheng.i18n.manager.I18nManager
import me.shouheng.i18n.manager.showWarn
import me.shouheng.i18n.view.i18n.DetectedPathListView
import me.shouheng.i18n.vm.I18nViewModel
import me.shouheng.i18n.widget.NormalDialogTitle
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource

/** 路径设置对话框 */
@Preview
@Composable
fun PathsSettingDialog(
    vm: I18nViewModel,
    onClose: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var directories by remember { mutableStateOf(vm.paths.value.map { PathDetected.from(it) }) }
    var toDelete by remember { mutableStateOf(mutableListOf<PathDetected>()) }
    Dialog(onDismissRequest = onClose) {
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
                NormalDialogTitle(stringResource(Res.string.folder_manage))
                // 路径
                DetectedPathListView(
                    directories,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 15.dp),
                ) {
                    val list = directories.toMutableList()
                    list.remove(it)
                    directories = list
                    if (it.path != null) {
                        toDelete.add(it)
                    }
                }
                // 底部按钮
                Row(
                    modifier = Modifier
                        .padding(vertical = 8.dp, horizontal = 15.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(onClick = {
                        vm.changePaths(directories, toDelete)
                        onClose()
                    }) {
                        Text(stringResource(Res.string.text_save), fontSize = 14.sp)
                    }
                    Button(onClick = {
                        scope.launch {
                            val file = FileKit.openDirectoryPicker(getString(Res.string.folder_choose))
                            file ?: return@launch
                            val matched = I18nManager.detect(file, vm.platform.value)
                            if (matched.isEmpty()) {
                                showWarn(Res.string.folder_not_found)
                                return@launch
                            }
                            val list = directories.toMutableList()
                            list.addAll(matched)
                            directories = list
                        }
                    }, colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.primary
                    )) {
                        Text(stringResource(Res.string.text_add), fontSize = 14.sp)
                    }
                    Spacer(Modifier.weight(1f))
                    Button(onClick = onClose) {
                        Text(stringResource(Res.string.text_cancel), fontSize = 14.sp)
                    }
                }
            }
        }
    }
}