package me.shouheng.i18n.view.i18n.wordlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import easy_i18n.composeapp.generated.resources.Res
import easy_i18n.composeapp.generated.resources.folder_current
import me.shouheng.i18n.utils.FileUtils
import me.shouheng.i18n.utils.extension.getResourceType
import me.shouheng.i18n.vm.I18nViewModel
import org.jetbrains.compose.resources.stringResource

/** 路径状态 */
@Composable
fun PathStateView(vm: I18nViewModel) {
    val content by vm.pathContent.collectAsState()
    val resourceType = content?.path?.getResourceType()
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            "${stringResource(Res.string.folder_current)}: ${content?.path?.path ?: ""}(${content?.words?.count() ?: 0})",
            fontSize = 10.sp,
            lineHeight = 15.sp,
            modifier = Modifier.alpha(0.8f)
                .weight(1f)
                .clickable {
                    FileUtils.openDirectory(content?.path)
                }
                .padding(horizontal = 10.dp),
            maxLines = 1
        )
        if (resourceType?.showEncoding == true) {
            EncodingView(vm)
        }
    }
}