package me.shouheng.i18n.view.settings.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import easy_i18n.composeapp.generated.resources.*
import me.shouheng.i18n.data.model.TranslatorType
import me.shouheng.i18n.manager.TranslatorManager
import me.shouheng.i18n.manager.showSuccess
import me.shouheng.i18n.vm.TranslatorSetterShareViewModel
import me.shouheng.i18n.widget.NormalDialogTitle
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun TranslatorSettingDialog(onClose: () -> Unit) {
    val vm = TranslatorSetterShareViewModel()
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
                NormalDialogTitle(stringResource(Res.string.set_i18n_api_title))
                // 内容
                InputRegionView(vm)
                // 底部按钮
                ButtonsView(
                    onSave = {
                        vm.triggerSave()
                        onClose()
                        showSuccess(Res.string.text_update_succeed)
                    },
                    onClose = onClose
                )
            }
        }
    }
}

/** 输入区域 */
@Composable
private fun InputRegionView(vm: TranslatorSetterShareViewModel) {
    var translator by remember { mutableStateOf(TranslatorManager.getTranslatorType() ?: TranslatorType.GOOGLE) }
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            stringResource(Res.string.set_i18n_api_hint),
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 15.dp).alpha(0.6f)
        )
        TranslatorTypePicker(translator) {
            translator = it
        }
        translator.getConfigureView(vm)
    }
}

/** 类型选择 */
@Composable
private fun TranslatorTypePicker(
    translator: TranslatorType,
    onTranslatorSelected: (TranslatorType) -> Unit
) {
    var open by remember { mutableStateOf(false) }
    Box {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { open = !open }
                .padding(horizontal = 15.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(stringResource(translator.nameRes))
            Spacer(modifier = Modifier.weight(1f))
            Icon(Icons.Default.ArrowDropDown, null)
        }
        DropdownMenu(
            expanded = open,
            onDismissRequest = { open = false },
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
                        open = false
                        onTranslatorSelected(it)
                    }
                )
            }
        }
    }
}

/** 底部按钮 */
@Composable
private fun ButtonsView(onSave: () -> Unit, onClose: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 15.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Spacer(Modifier.weight(1f))
        Button(onClick = onClose, colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.primary
        ),) {
            Text(stringResource(Res.string.text_cancel), fontSize = 14.sp)
        }
        Button(onClick = onSave) {
            Text(stringResource(Res.string.text_save), fontSize = 14.sp)
        }
    }
}