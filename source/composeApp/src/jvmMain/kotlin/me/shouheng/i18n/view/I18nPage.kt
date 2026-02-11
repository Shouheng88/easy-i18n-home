package me.shouheng.i18n.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import easy_i18n.composeapp.generated.resources.*
import me.shouheng.i18n.I18nProject
import me.shouheng.i18n.data.Colors
import me.shouheng.i18n.data.UIConst
import me.shouheng.i18n.data.model.I18nPlatform
import me.shouheng.i18n.utils.extension.alpha
import me.shouheng.i18n.view.i18n.PathsManagerView
import me.shouheng.i18n.view.i18n.WordListView
import me.shouheng.i18n.view.i18n.dialog.ProjectEditDialog
import me.shouheng.i18n.view.i18n.dialog.ProjectsManageDialog
import me.shouheng.i18n.view.info.dialog.DocumentDialog
import me.shouheng.i18n.vm.I18nViewModel
import me.shouheng.i18n.widget.PageTitle
import me.shouheng.i18n.widget.PageToolbar
import me.shouheng.i18n.widget.SimpleIconButton
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/** 多语言管理 */
@Composable
fun I18nPage(vm: I18nViewModel) {
    LaunchedEffect(Unit) {
        vm.loadProjects()
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column {
            Toolbar(vm)
            PageBody(vm)
        }
    }
}

/** 主页面*/
@Composable
private fun PageBody(vm: I18nViewModel) {
    val projects by vm.projects.collectAsState()
    val paths by vm.paths.collectAsState()
    val firstLoading by vm.firstLoading.collectAsState()
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (firstLoading == false) {
            if (projects.isEmpty()) {
                EmptyProjects(vm)
            } else {
                if (paths.isEmpty()) {
                    PathsManagerView(vm)
                } else {
                    WordListView(vm)
                }
            }
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(30.dp)
                )
                Text(stringResource(Res.string.text_loading))
            }
        }
    }
}

/** 项目为空时页面。*/
@Composable
private fun EmptyProjects(vm: I18nViewModel) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        var openCreateDialog by remember { mutableStateOf(false) }
        Image(
            imageResource(Res.drawable.ic_archive),
            contentDescription = null,
            modifier = Modifier.size(100.dp),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
        )
        Text(
            stringResource(Res.string.project_empty_title),
            textAlign = TextAlign.Center,
            fontSize = 15.sp,
            style = TextStyle(fontWeight = FontWeight.Bold),
            modifier = Modifier.alpha(0.9f).padding(horizontal = 80.dp)
        )
        Button(
            onClick = { openCreateDialog = true }
        ) {
            Text(stringResource(Res.string.project_create), style = TextStyle(color = Color.White))
        }
        if (openCreateDialog) {
            ProjectEditDialog(
                onConfirm = { name, description ->
                    vm.createProject(name, description)
                    openCreateDialog = false
                },
                onClose = { openCreateDialog = false }
            )
        }
    }
}

/** 工具栏。*/
@Composable
private fun Toolbar(vm: I18nViewModel) {
    val project by vm.project.collectAsState()
    val platform by vm.platform.collectAsState()
    var showDocumentDialog by remember { mutableStateOf(false) }
    PageToolbar {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            PageTitle(stringResource(Res.string.tab_i18n_name), Icons.Default.Dataset)
            if (project != null) {
                ProjectListView(project, vm)
                PlatformListView(platform, vm)
                // 刷新按钮
                SimpleIconButton(Icons.Default.Refresh) {
                    // 刷新
                    vm.reloadWords()
                }
            }
        }
        SimpleIconButton(Icons.Default.Lightbulb, Colors.lightbulb, Colors.lightbulb.alpha(.1f)) {
            showDocumentDialog = true
        }
    }
    if (showDocumentDialog) {
        DocumentDialog { showDocumentDialog = false }
    }
}

/** 项目 选择-列表 */
@Composable
private fun ProjectListView(
    project: I18nProject?,
    vm: I18nViewModel
) {
    val projects by vm.projects.collectAsState()
    var openCreateDialog by remember { mutableStateOf(false) }
    var openManageDialog by remember { mutableStateOf(false) }
    Box {
        var expandedProjects by remember { mutableStateOf(false) }
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .clickable { expandedProjects = true }
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .padding(start = 15.dp, end = 8.dp)
                .height(UIConst.ICON_BUTTON_SIZE.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(project?.name ?: "", fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)
            Icon(Icons.Default.ArrowDropDown, null, tint = MaterialTheme.colorScheme.primary)
        }
        DropdownMenu(
            expanded = expandedProjects,
            onDismissRequest = { expandedProjects = false },
            modifier = Modifier.heightIn(max = 400.dp)
        ) {
            DropdownMenuItem(
                text = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Text(stringResource(Res.string.project_create), fontSize = 15.sp)
                    }
                },
                onClick = {
                    openCreateDialog = true
                    expandedProjects = false
                }
            )
            DropdownMenuItem(
                text = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Settings, contentDescription = null)
                        Text(stringResource(Res.string.project_manage), fontSize = 15.sp)
                    }
                },
                onClick = {
                    openManageDialog = true
                    expandedProjects = false
                }
            )
            DropdownMenuItem(
                text = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        HorizontalDivider(modifier = Modifier.height(0.5.dp).fillMaxWidth())
                        Text(stringResource(Res.string.project_list_tip, projects.size),
                            fontSize = 12.sp,
                            modifier = Modifier.alpha(0.6f))
                    }
                },
                onClick = {}
            )
            projects.forEach {
                DropdownMenuItem(
                    text = {
                        Text(it.name, fontSize = 16.sp, modifier = Modifier)
                    },
                    onClick = {
                        vm.setWorkingProject(it)
                        expandedProjects = false
                    }
                )
            }
        }
        // 项目-创建 对话框
        if (openCreateDialog) {
            ProjectEditDialog(
                onConfirm = { name, description ->
                    vm.createProject(name, description)
                    openCreateDialog = false
                },
                onClose = { openCreateDialog = false }
            )
        }
        // 项目-管理 对话框
        if (openManageDialog) {
            ProjectsManageDialog(vm, onClose = { openManageDialog = false })
        }
    }
}

/** 平台 列表 */
@Composable
private fun PlatformListView(platform: I18nPlatform, vm: I18nViewModel) {
    Box {
        var expandedPlatforms by remember { mutableStateOf(false) }
        SimpleIconButton(painterResource(platform.icon)) {
            expandedPlatforms = true
        }
        DropdownMenu(
            expanded = expandedPlatforms,
            onDismissRequest = { expandedPlatforms = false }
        ) {
            I18nPlatform.entries.forEach {
                DropdownMenuItem(
                    text = {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painterResource(it.icon),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(UIConst.ICON_BUTTON_SIZE.dp, UIConst.ICON_BUTTON_SIZE.dp)
                                    .padding(UIConst.ICON_BUTTON_PADDING.dp)
                            )
                            Text(it.displayName.get(), fontSize = 16.sp, modifier = Modifier)
                        }
                    },
                    onClick = {
                        vm.setWorkingPlatform(it)
                        expandedPlatforms = false
                    }
                )
            }
        }
    }
}
