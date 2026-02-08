package me.shouheng.i18n.view.i18n.wordlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.shouheng.i18n.manager.TextManager
import me.shouheng.i18n.vm.I18nViewModel

/** 底部的编码方式展示和选择 */
@Composable
fun EncodingView(vm: I18nViewModel) {
    val content by vm.pathContent.collectAsState()
    val path = content?.path
    val encoding = path?.encoding ?: TextManager.JAVA_PROPERTIES_DEFAULT_ENCODING
    var open by remember { mutableStateOf(false) }
    Box {
        Text(
            encoding,
            fontSize = 10.sp,
            lineHeight = 15.sp,
            modifier = Modifier.alpha(0.8f)
                .clickable { open = true }
                .padding(horizontal = 10.dp),
            maxLines = 1
        )
        DropdownMenu(
            expanded = open,
            modifier = Modifier,
            onDismissRequest = { open = false }
        ) {
            listOf("UTF-8", "UTF-16", "US-ASCII", "ISO-8859-1", "UTF-32", "GB2312").forEach { encoding ->
                DropdownMenuItem(
                    text = { Text(encoding) },
                    onClick = {
                        path ?: return@DropdownMenuItem
                        vm.updateEncoding(path, encoding)
                    }
                )
            }
        }
    }
}