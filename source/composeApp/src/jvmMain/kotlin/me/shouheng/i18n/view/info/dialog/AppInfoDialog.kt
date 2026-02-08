package me.shouheng.i18n.view.info.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import easy_i18n.composeapp.generated.resources.Res
import easy_i18n.composeapp.generated.resources.tab_info_name
import easy_i18n.composeapp.generated.resources.text_got_it
import me.shouheng.i18n.manager.AppManger
import me.shouheng.i18n.manager.DebugManager
import me.shouheng.i18n.utils.*
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import java.text.SimpleDateFormat
import java.util.*

/** 应用信息展示对话框 */
@Preview
@Composable
fun AppInfoDialog(
    onClose: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onClose,
        title = { Text(stringResource(Res.string.tab_info_name)) },
        text = {
            Column {
                AppInfoRowView("Unique ID", DeviceIdFactory.get())
                AppInfoRowView("Device", DeviceUtils.getDeviceType().name)
                AppInfoRowView("OS", "${DeviceUtils.getOSName()} | ${DeviceUtils.getOSVersion()} | ${DeviceUtils.getOSArch()}")
                AppInfoRowView("Ver", AppUtils.getAppVersionName() + " (${AppUtils.getBuild()})")
                AppInfoRowView("Env", "${AppUtils.getEnv()}")
                AppInfoRowView("Debug", "${isPossibleDebug()}")
                AppInfoRowView("Install", SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Date(AppManger.getInstallTime())))
                AppInfoRowView("Crashes", "${DebugManager.getCrashLogCount()}", modifier = Modifier.clickable {
                    val dir = DebugManager.getCrashesDirectory()
                    if (dir.exists()) {
                        FileUtils.openDirectory(dir)
                    }
                })
            }
        },
        confirmButton = {
            TextButton(onClick = onClose) {
                Text(stringResource(Res.string.text_got_it))
            }
        }
    )
}

@Composable
private fun AppInfoRowView(
    name: String,
    value: String,
    valueColor: Color = Color.Unspecified,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        Text(name, textAlign = TextAlign.End, modifier = Modifier.weight(1f))
        Text(value, modifier = Modifier.weight(1f), color = valueColor)
    }
}