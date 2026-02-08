package me.shouheng.i18n.view.i18n

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Search
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
import easy_i18n.composeapp.generated.resources.Res
import easy_i18n.composeapp.generated.resources.folder_add
import easy_i18n.composeapp.generated.resources.folder_empty_subtitle
import easy_i18n.composeapp.generated.resources.folder_empty_title
import easy_i18n.composeapp.generated.resources.folder_scan_result
import easy_i18n.composeapp.generated.resources.folder_choose
import easy_i18n.composeapp.generated.resources.ic_archive
import easy_i18n.composeapp.generated.resources.ic_list
import easy_i18n.composeapp.generated.resources.text_save
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.openDirectoryPicker
import kotlinx.coroutines.launch
import me.shouheng.i18n.data.model.PathDetected
import me.shouheng.i18n.vm.I18nViewModel
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.stringResource

/** 应用的路径管理 */
@Composable
fun PathsManagerView(vm: I18nViewModel) {
    val directories by vm.detectedDirectories.collectAsState()
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.weight(1f)
        ) {
            if (directories.isEmpty()) {
                DirectoriesEmptyView()
            } else {
                DetectedPathListView(directories, onDelete = {
                    vm.removeDetectedDirectory(it)
                })
            }
        }
        BottomToolbar(directories, vm)
    }
}

/** 底部工具栏 */
@Composable
private fun BottomToolbar(
    directories: List<PathDetected>,
    vm: I18nViewModel,
) {
    val scope = rememberCoroutineScope()
    Row(
        modifier = Modifier.padding(vertical = 15.dp, horizontal = 15.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(modifier = Modifier.weight(1f)) {  }
        Button(onClick = {
            scope.launch {
                val file = FileKit.openDirectoryPicker(getString(Res.string.folder_choose))
                file ?: return@launch
                vm.detectDirectories(file)
            }
        }) {
            Text(stringResource(Res.string.folder_add), style = TextStyle(color = Color.White))
        }
        Button(enabled = directories.isNotEmpty(), onClick = {
            vm.savePaths(directories)
        }) {
            Text(stringResource(Res.string.text_save), style = TextStyle(color = Color.White))
        }
    }
}

/** 选择的路径为空页面 */
@Composable
private fun DirectoriesEmptyView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                imageResource(Res.drawable.ic_list),
                contentDescription = null,
                modifier = Modifier.size(100.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
            )
            Text(
                stringResource(Res.string.folder_empty_title),
                textAlign = TextAlign.Center,
                fontSize = 13.sp,
                style = TextStyle(fontWeight = FontWeight.Bold),
                modifier = Modifier.alpha(0.9f).padding(horizontal = 80.dp).padding(top = 20.dp)
            )
            Text(
                stringResource(Res.string.folder_empty_subtitle),
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(0.9f).padding(horizontal = 40.dp)
            )
        }
    }
}

/** 选择的路径列表 */
@Composable
fun DetectedPathListView(
    directories: List<PathDetected>,
    modifier: Modifier = Modifier.padding(15.dp),
    onDelete: (PathDetected) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                Icons.Default.Search,
                null,
                modifier = Modifier.size(16.dp)
            )
            Text(
                stringResource(Res.string.folder_scan_result),
                fontSize = 12.sp
            )
        }
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            directories.forEachIndexed { index, directory ->
                PathListItem(index, directory, onDelete)
            }
        }
    }
}

/** 选择的路径 */
@Composable
private fun PathListItem(
    index: Int,
    directory: PathDetected,
    onDelete: (PathDetected) -> Unit,
) {
    val iconSize = 30
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(30.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(vertical = 10.dp, horizontal = 10.dp)
    ) {
        Text(
            "${index+1}",
            fontSize = 14.sp,
            style = TextStyle(fontWeight = FontWeight.Bold),
            modifier = Modifier
                .size(iconSize.dp, iconSize.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .wrapContentSize(Alignment.Center)
        )
        Text(directory.getSimplifiedPath(), fontSize = 13.sp, modifier = Modifier.weight(1f))
        Icon(
            Icons.Default.Delete,
            contentDescription = null,
            modifier = Modifier
                .size(iconSize.dp, iconSize.dp)
                .clip(CircleShape)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(bounded = true, radius = iconSize.dp)
                ) {
                    // 删除目录
                    onDelete(directory)
                }
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .padding(6.dp),
            tint = Color.Red
        )
    }
}