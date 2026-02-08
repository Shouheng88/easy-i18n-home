package me.shouheng.i18n.view.settings

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import com.russhwolf.settings.contains
import me.shouheng.i18n.data.Event
import me.shouheng.i18n.data.UIConst
import me.shouheng.i18n.db.databaseFile
import me.shouheng.i18n.manager.post
import me.shouheng.i18n.manager.showError
import me.shouheng.i18n.manager.showSuccess
import me.shouheng.i18n.utils.DeviceUtils
import me.shouheng.i18n.utils.settings
import me.shouheng.i18n.widget.SettingItemView
import me.shouheng.i18n.widget.SettingSectionCard
import me.shouheng.i18n.widget.SimpleInputDialog

@Composable
fun DebugSettingCard() {
    val apiState by remember { mutableStateOf("") }
    var showDeleteKeyDialog by remember { mutableStateOf(false) }
    val uriHandler = LocalUriHandler.current
    val clipboard = LocalClipboardManager.current
    SettingSectionCard(
        "开发者工具",
        Modifier.fillMaxWidth(UIConst.SETTING_ITEM_MAX_FRACTION)
    ) {
        SettingItemView(
            Icons.Default.FolderDelete,
            "删除 settings 键值对",
            onClick = { showDeleteKeyDialog = true }
        )
        SettingItemView(
            Icons.Default.BugReport,
            "抛出一个异常",
            onClick = { 1 / 0 }
        )
        SettingItemView(
            Icons.Default.Info,
            "输出应用信息",
            onClick = {
                println(DeviceUtils.getOSName())
                println(DeviceUtils.getOSVersion())
                println(DeviceUtils.getOSArch())
                println(DeviceUtils.getJavaVersion())
                println(DeviceUtils.getFirstValidMacAddress())
                println(DeviceUtils.getCurrentNetworkType())
            }
        )
        SettingItemView(
            Icons.Default.Language,
            "测试打开邮件链接",
            onClick = {
                uriHandler.openUri("mailto:shouheng2020@gmail.com")
                clipboard.setText(AnnotatedString("要复制到剪贴板的文本"))
            }
        )
        SettingItemView(
            Icons.Default.CalendarMonth,
            "模拟会话过期提示",
            onClick = {
                post(Event.EVENT_SESSION_EXPIRED)
            }
        )
        SettingItemView(
            Icons.Default.Dataset,
            "删除数据库",
            onClick = {
                val deleted = databaseFile.delete()
                println(if (deleted) "已删除" else "删除失败")
            }
        )
    }
    if (showDeleteKeyDialog) {
        SimpleInputDialog(
            "删除 settings 记录",
            hint = "输入要删除的键",
            onDismiss = { showDeleteKeyDialog = false },
            onConfirm = {
                if (settings.contains(it)) {
                    settings.remove(it)
                    showSuccess("已删除")
                    showDeleteKeyDialog = false
                } else {
                    showError { "指定的键不存在" }
                }
            }
        )
    }
}