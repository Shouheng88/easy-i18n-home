package me.shouheng.i18n.widget

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import easy_i18n.composeapp.generated.resources.*
import me.shouheng.i18n.manager.AppManger
import me.shouheng.i18n.vm.InfoViewModel
import org.jetbrains.compose.resources.stringResource

/** 升级对话框 */
@Preview
@Composable
fun UpgradeDialog(
    vmInfo: InfoViewModel
) {
    val uriHandler = LocalUriHandler.current
    val isForceUpgrade = AppManger.isForceUpgrade()
    val version = AppManger.getNewVersion()
    val description = "fixed some bugs"
    var isIgnored by remember { mutableStateOf(AppManger.isVersionIgnored(version)) }
    AlertDialog(
        onDismissRequest = {
            if (!isForceUpgrade) {
                vmInfo.showAppUpgradeDialog.value = false
            }
        },
        title = { Text(stringResource(Res.string.info_new_version_found, version)) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 300.dp)
                        .verticalScroll(ScrollState(0)),
                    verticalArrangement = Arrangement.Top,
                ) {
                    Text(description)
                }
                if (!isForceUpgrade) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.clickable {
                            isIgnored = !isIgnored
                            AppManger.changeIgnoreState(version, isIgnored)
                        }
                    ) {
                        Icon(
                            if (isIgnored) Icons.Default.CheckBox else Icons.Default.CheckBoxOutlineBlank,
                            contentDescription = null
                        )
                        Text(stringResource(Res.string.info_ignore_version), fontSize = 13.sp)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {

            }) {
                Text(stringResource(Res.string.info_upgrade_now))
            }
        },
        dismissButton = {
            if (!isForceUpgrade) {
                TextButton(onClick = { vmInfo.showAppUpgradeDialog.value = false }) {
                    Text(stringResource(Res.string.text_cancel))
                }
            }
        }
    )
}