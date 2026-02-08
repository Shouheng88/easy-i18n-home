package me.shouheng.i18n.view.i18n.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import easy_i18n.composeapp.generated.resources.Res
import easy_i18n.composeapp.generated.resources.project_create_title
import easy_i18n.composeapp.generated.resources.project_desc
import easy_i18n.composeapp.generated.resources.project_desc_desc
import easy_i18n.composeapp.generated.resources.project_name
import easy_i18n.composeapp.generated.resources.project_name_empty_tip
import easy_i18n.composeapp.generated.resources.text_cancel
import easy_i18n.composeapp.generated.resources.text_save
import me.shouheng.i18n.I18nProject
import me.shouheng.i18n.widget.NormalDialogFooter
import me.shouheng.i18n.widget.NormalDialogTitle
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/** 多语言项目编辑对话框 */
@Preview
@Composable
fun ProjectEditDialog(
    title: String = stringResource(Res.string.project_create_title),
    project: I18nProject? = null,
    onConfirm: (name: String, description: String) -> Unit,
    onClose: () -> Unit
) {
    var name by remember { mutableStateOf(project?.name ?: "") }
    var isNameError by remember { mutableStateOf(false) }
    var description by remember { mutableStateOf(project?.description ?: "") }
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
                NormalDialogTitle(title)
                // 输入框
                Column(
                    modifier = Modifier
                        .padding(horizontal = 15.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // 名称
                    OutlinedTextField(
                        value = name,
                        textStyle = TextStyle(fontSize = 14.sp),
                        onValueChange = { value ->
                            name = value
                            isNameError = false
                        },
                        label = { Text(stringResource(Res.string.project_name)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isError = isNameError,
                        supportingText = {
                            if (isNameError) {
                                Text(
                                    stringResource(Res.string.project_name_empty_tip),
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    )
                    // 描述
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        OutlinedTextField(
                            value = description,
                            textStyle = TextStyle(fontSize = 14.sp),
                            onValueChange = { value ->
                                description = value
                            },
                            label = { Text(stringResource(Res.string.project_desc)) },
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 5
                        )
                        Text(
                            stringResource(Res.string.project_desc_desc),
                            fontSize = 12.sp,
                            lineHeight = 15.sp
                        )
                    }
                }
                // 底部操作区
                NormalDialogFooter(
                    left = stringResource(Res.string.text_cancel),
                    right = stringResource(Res.string.text_save),
                    onLeft = onClose,
                    onRight = {
                        val finalName = name.trim()
                        if (finalName.isEmpty()) {
                            isNameError = true
                            return@NormalDialogFooter
                        }
                        onConfirm(finalName, description)
                    }
                )
            }
        }
    }
}