package me.shouheng.i18n.view.i18n.wordlist

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import easy_i18n.composeapp.generated.resources.Res
import easy_i18n.composeapp.generated.resources.folder_manage
import easy_i18n.composeapp.generated.resources.folder_switch
import me.shouheng.i18n.data.model.I18nResourceType
import me.shouheng.i18n.view.i18n.dialog.PathsSettingDialog
import me.shouheng.i18n.vm.I18nViewModel
import me.shouheng.i18n.widget.SmallIconButton
import org.jetbrains.compose.resources.stringResource

/** 当前工作的路径 */
@Composable
fun SwitchPathView(vm: I18nViewModel) {
    val paths by vm.paths.collectAsState()
    var expandedPaths by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }
    Box {
        SmallIconButton(Icons.Default.Folder) {
            expandedPaths = true
        }
        DropdownMenu(
            expanded = expandedPaths,
            onDismissRequest = { expandedPaths = false }
        ) {
            DropdownMenuItem(
                text = {
                    Text(stringResource(Res.string.folder_switch), fontWeight = FontWeight.Bold)
                },
                onClick = {}
            )
            paths.forEach {
                val type = I18nResourceType.from(it.resourceType.toInt())
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Default.Folder,
                                null,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                "${it.path}(${type.simpleName()})",
                                fontSize = 12.sp,
                                modifier = Modifier
                                    .padding(vertical = 4.dp),
                                maxLines = 3
                            )
                        }
                    },
                    onClick = {
                        vm.loadWords(it)
                        expandedPaths = false
                    }
                )
            }
            DropdownMenuItem(
                text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Default.Settings,
                            null,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            stringResource(Res.string.folder_manage),
                            fontSize = 12.sp,
                            modifier = Modifier
                                .padding(vertical = 4.dp),
                            maxLines = 3
                        )
                    }
                },
                onClick = {
                    expandedPaths = false
                    showSettingsDialog = true
                }
            )
        }
    }
    if (showSettingsDialog) {
        PathsSettingDialog(vm, onClose = {
            showSettingsDialog = false
        })
    }
}