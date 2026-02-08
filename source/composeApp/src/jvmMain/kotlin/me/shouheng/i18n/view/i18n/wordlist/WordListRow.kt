package me.shouheng.i18n.view.i18n.wordlist

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.shouheng.i18n.data.model.I18nWordListBodyUICell
import me.shouheng.i18n.data.model.I18nWordListBodyUIRow
import me.shouheng.i18n.data.model.I18nWordListHeaderUICell
import me.shouheng.i18n.view.i18n.dialog.WordEditDialog
import me.shouheng.i18n.vm.I18nViewModel
import org.jetbrains.compose.resources.painterResource

/** 单词列表：行 */
@Composable
fun WordListRow(
    vm: I18nViewModel,
    item: I18nWordListBodyUIRow,
    scrollState: ScrollState,
) {
    var selectedRow by remember { mutableStateOf<I18nWordListBodyUIRow?>(null) }
    val ignored by vm.ignoreLanguages.collectAsState()
    WordListRowCellGroupView(
        item,
        scrollState,
        setWidth = { fillMaxWidth() },
        onRowSelected = {
            selectedRow = it
        }
    ) {
        item.items.filter {
            it.type != I18nWordListHeaderUICell.HEADER_ID_LANG || !ignored.contains(it.language ?: "")
        }.forEachIndexed { _, item ->
            WordListRowCellView(item)
        }
    }
    // 选择了某个词汇
    if (selectedRow != null) {
        val row = selectedRow!!
        val word = row.word
        val itemItems = row.getEditItems(vm.ignoreLanguages.value)
        WordEditDialog(word, itemItems, vm) {
            selectedRow = null
        }
    }
}

/** 单词列表单元格群组 */
@Composable
private fun WordListRowCellGroupView(
    item: I18nWordListBodyUIRow,
    scrollState: ScrollState,
    setWidth: Modifier.() -> Modifier,
    onRowSelected: (I18nWordListBodyUIRow) -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = Modifier
            .background(if (item.index % 2 == 0) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent)
            .setWidth()
            .clickable {
                onRowSelected(item)
            }
            .horizontalScroll(scrollState),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        content = content
    )
}

/** 列表的单元格 */
@Composable
private fun WordListRowCellView(item: I18nWordListBodyUICell) {
    if (item.icon != null) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painterResource(item.icon),
                contentDescription = null,
                modifier = Modifier
                    .width(item.width.dp)
                    .height(12.dp)
                    .alpha(0.4f),
                tint = item.iconColor!!
            )
            Text(item.textGetter.get(), fontSize = 10.sp)
        }
    } else {
        Text(
            item.textGetter.get(),
            maxLines = 3,
            textAlign = if (item.textCenter) TextAlign.Center else TextAlign.Start,
            overflow = TextOverflow.Ellipsis,
            style = TextStyle(fontSize = item.textSize.sp),
            modifier = Modifier.width(item.width.dp).padding(vertical = 4.dp)
        )
    }
}