package me.shouheng.i18n.view.info

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import easy_i18n.composeapp.generated.resources.Res
import easy_i18n.composeapp.generated.resources.feedback_subtitle
import easy_i18n.composeapp.generated.resources.feedback_title
import me.shouheng.i18n.data.UIConst
import me.shouheng.i18n.view.info.dialog.FeedbackDialog
import me.shouheng.i18n.vm.InfoViewModel
import me.shouheng.i18n.widget.SettingItemView
import me.shouheng.i18n.widget.SettingSectionCard
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/** 应用的基础信息 */
@Preview
@Composable
fun AppInfoCard(vm: InfoViewModel) {
    var showFeedbackDialog by remember { mutableStateOf(false) }
    SettingSectionCard(
        modifier = Modifier
            .fillMaxWidth(UIConst.SETTING_ITEM_MAX_FRACTION)
            .padding(top = 20.dp)
    ) {
        SettingItemView(
            Icons.Default.Feedback,
            stringResource(Res.string.feedback_title),
            stringResource(Res.string.feedback_subtitle),
            onClick = { showFeedbackDialog = true }
        )
    }
    if (showFeedbackDialog) {
        FeedbackDialog(vm, onClose = { showFeedbackDialog = false })
    }
}