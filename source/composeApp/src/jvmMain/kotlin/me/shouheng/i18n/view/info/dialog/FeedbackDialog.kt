package me.shouheng.i18n.view.info.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import easy_i18n.composeapp.generated.resources.*
import kotlinx.coroutines.flow.collectLatest
import me.shouheng.i18n.data.Colors
import me.shouheng.i18n.net.server.model.enums.FeedbackType
import me.shouheng.i18n.vm.InfoViewModel
import me.shouheng.i18n.widget.NormalDialogFooter
import me.shouheng.i18n.widget.NormalDialogTitle
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/** 反馈对话框 */
@Preview
@Composable
fun FeedbackDialog(
    vm: InfoViewModel,
    onClose: () -> Unit,
) {
    var feedbackType by remember { mutableStateOf(FeedbackType.IMPROVEMENT) }
    var content by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var isContentEmptyError by remember { mutableStateOf(false) }
    val state by vm.feedbackState.collectAsState()
    var showSucceedDialog by remember { mutableStateOf(false) }
    var failedMessage by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        vm.feedbackState.collectLatest {
            if (it?.isSuccess == true) {
                showSucceedDialog = true
            } else if (it?.isFailure == true) {
                failedMessage = "${vm.feedbackState.value?.code}:${vm.feedbackState.value?.message}"
            }
            vm.feedbackState.value = null
        }
    }
    Dialog(onDismissRequest = onClose) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .fillMaxWidth(0.9f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 标题
                NormalDialogTitle(stringResource(Res.string.feedback_title))
                // 类型
                FeedbackTypesView(feedbackType) {
                    feedbackType = it
                }
                // 反馈内容
                OutlinedTextField(
                    value = content,
                    textStyle = TextStyle(fontSize = 14.sp),
                    onValueChange = { value ->
                        content = value
                        isContentEmptyError = false
                    },
                    label = { Text(stringResource(Res.string.feedback_content_hint)) },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp),
                    singleLine = false,
                    maxLines = 3,
                    isError = isContentEmptyError
                )
                if (isContentEmptyError) {
                    Text(stringResource(Res.string.feedback_content_empty), fontSize = 12.sp, color = Colors.error, modifier = Modifier.padding(horizontal = 15.dp))
                }
                // 联系方式
                OutlinedTextField(
                    value = contact,
                    textStyle = TextStyle(fontSize = 14.sp),
                    onValueChange = { value ->
                        contact = value
                    },
                    label = { Text(stringResource(Res.string.feedback_contact_hint)) },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp),
                    singleLine = true
                )
                Text(stringResource(Res.string.feedback_contact_tip), fontSize = 12.sp, modifier = Modifier.padding(horizontal = 15.dp))
                // 底部按钮
                if (state?.isProgress == true) {
                    Row(
                        modifier = Modifier.defaultMinSize(minHeight = 44.dp)
                    ) {
                        Spacer(Modifier.weight(1f))
                        Text(stringResource(Res.string.text_sending), fontSize = 14.sp)
                        CircularProgressIndicator(
                            modifier = Modifier.size(30.dp)
                        )
                    }
                } else {
                    // 底部按钮
                    NormalDialogFooter(
                        left = stringResource(Res.string.text_cancel),
                        right = stringResource(Res.string.text_done),
                        onLeft = onClose,
                        onRight = {
                            val content = content.trim()
                            if (content.isEmpty()) {
                                isContentEmptyError = true
                                return@NormalDialogFooter
                            }
                        }
                    )
                }
            }
        }
    }
    if (showSucceedDialog) {
        AlertDialog(
            onDismissRequest = { showSucceedDialog = false },
            title = { Text(stringResource(Res.string.text_succeed)) },
            text = { Text(stringResource(Res.string.feedback_succeed_message)) },
            confirmButton = {
                TextButton(onClick = {
                    showSucceedDialog = false
                    onClose()
                }) {
                    Text(stringResource(Res.string.text_got_it))
                }
            },
        )
    }
    if (failedMessage.isNotEmpty()) {
        AlertDialog(
            onDismissRequest = { failedMessage = "" },
            title = { Text(stringResource(Res.string.text_failed)) },
            text = { Text(failedMessage) },
            confirmButton = {
                TextButton(onClick = { failedMessage = "" }) {
                    Text(stringResource(Res.string.text_got_it))
                }
            })
    }
}

@Composable
private fun FeedbackTypesView(type: FeedbackType, onChanged: (FeedbackType) -> Unit) {
    var expandedTypes by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier.clickable {
            expandedTypes = true
        }.padding(vertical = 10.dp, horizontal = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(stringResource(Res.string.feedback_type))
        Spacer(modifier = Modifier.weight(1f))
        Box {
            Text(stringResource(type.nameRes), fontSize = 13.sp)
            DropdownMenu(
                expanded = expandedTypes,
                onDismissRequest = { expandedTypes = false },
            ) {
                FeedbackType.entries.forEach {
                    DropdownMenuItem(
                        text = {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(stringResource(it.nameRes), fontSize = 15.sp)
                            }
                        },
                        onClick = {
                            onChanged(it)
                            expandedTypes = false
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