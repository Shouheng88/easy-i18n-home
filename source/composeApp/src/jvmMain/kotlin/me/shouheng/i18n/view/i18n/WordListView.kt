package me.shouheng.i18n.view.i18n

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.shouheng.i18n.view.i18n.wordlist.WordListFooter
import me.shouheng.i18n.view.i18n.wordlist.WordListHeader
import me.shouheng.i18n.view.i18n.wordlist.WordListRow
import me.shouheng.i18n.vm.I18nViewModel

@Composable
fun WordListView(
    vm: I18nViewModel,
) {
    val content by vm.pathContent.collectAsState()
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // 表头
        WordListHeader(vm, scrollState)
        // 内容
        LazyColumn (
            modifier = Modifier.weight(1f)
        ) {
            // 加载列表项
            items(content?.rows ?: emptyList()) { item ->
                WordListRow(vm, item, scrollState)
            }
        }
        HorizontalDivider(modifier = Modifier.height(0.5.dp).fillMaxWidth())
        // 底部操作按钮
        WordListFooter(vm)
    }
}
