package me.shouheng.i18n.view.i18n.dialog

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import easy_i18n.composeapp.generated.resources.*
import me.shouheng.i18n.I18nProject
import me.shouheng.i18n.vm.I18nViewModel
import me.shouheng.i18n.widget.NormalDialogTitle
import org.jetbrains.compose.resources.stringResource

/** 项目管理对话框 */
@Preview
@Composable
fun ProjectsManageDialog(
    vm: I18nViewModel,
    onClose: () -> Unit,
) {
    val projects by vm.projects.collectAsState()
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
                NormalDialogTitle(stringResource(Res.string.project_manage))
                // 项目列表
                Column(
                    modifier = Modifier
                        .padding(horizontal = 15.dp)
                        .verticalScroll(rememberScrollState())
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    projects.forEach { project ->
                        ProjectListItem(project, vm)
                    }
                }
                // 底部操作区
                Row(
                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 15.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Spacer(Modifier.weight(1f))
                    Button(
                        onClick = onClose
                    ) {
                        Text(stringResource(Res.string.text_cancel), fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ProjectListItem(project: I18nProject, vm: I18nViewModel) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    val iconSize = 30
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(15.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(horizontal = 15.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text("${stringResource(Res.string.project_name)}: ${project.name}", fontSize = 15.sp)
            if (!project.description.isNullOrEmpty()) {
                val description = project.description.replace('\n', ' ').replace('\t', ' ')
                Text(
                    description,
                    fontSize = 13.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Icon(
            Icons.Default.Edit,
            null,
            modifier = Modifier
                .size(iconSize.dp, iconSize.dp)
                .clip(CircleShape)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(bounded = true, radius = iconSize.dp)
                ) { showEditDialog = true }
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .padding(6.dp)
        )
        Icon(
            Icons.Default.Delete,
            null,
            tint = Color.Red,
            modifier = Modifier
                .size(iconSize.dp, iconSize.dp)
                .clip(CircleShape)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(bounded = true, radius = iconSize.dp)
                ) { showDeleteDialog = true }
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .padding(6.dp)
        )
    }
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(Res.string.project_delete_title)) },
            text = { Text(stringResource(Res.string.project_delete_message, project.name)) },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    vm.deleteProject(project)
                }) {
                    Text(stringResource(Res.string.text_sure))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(stringResource(Res.string.text_cancel))
                }
            }
        )
    }
    if (showEditDialog) {
        ProjectEditDialog(
            title = stringResource(Res.string.project_edit),
            project = project,
            onClose = { showEditDialog = false },
            onConfirm = { name, description ->
                vm.editProject(project, name, description)
                showEditDialog = false
            }
        )
    }
}
