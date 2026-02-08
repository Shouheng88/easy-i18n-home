package me.shouheng.i18n.view.i18n.wordlist

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.shouheng.i18n.data.model.I18nWordListHeaderUICell
import me.shouheng.i18n.vm.I18nViewModel

/** 表格-表头 */
@Composable
fun WordListHeader(
    vm: I18nViewModel,
    scrollState: ScrollState,
) {
    val content by vm.pathContent.collectAsState()
    val ignored by vm.ignoreLanguages.collectAsState()
    WordListHeaderCellGroupView(scrollState, { fillMaxWidth() }) {
        content?.headers?.filter {
            it.type != I18nWordListHeaderUICell.HEADER_ID_LANG || !ignored.contains(it.language ?: "")
        }?.forEachIndexed { _, item ->
            WordListHeaderCellView(vm, item)
        }
    }
}

@Composable
private fun WordListHeaderCellGroupView(
    scrollState: ScrollState,
    setWidth: Modifier.() -> Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary)
            .setWidth()
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        content
    )
}

/** 单元格 */
@Composable
private fun WordListHeaderCellView(
    vm: I18nViewModel,
    item: I18nWordListHeaderUICell,
) {
    Text(
        item.nameGetter.get(),
        textAlign = if (item.textCenter) TextAlign.Center else TextAlign.Start,
        style = TextStyle(fontSize = 14.sp),
        color = MaterialTheme.colorScheme.onPrimary,
        maxLines = 1,
        overflow = TextOverflow.MiddleEllipsis,
        modifier = Modifier.width(item.width.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true)
            ) {
                when(item.type) {
                    I18nWordListHeaderUICell.HEADER_ID_NAME -> {
                        vm.nextNameOrder()
                    }
                    I18nWordListHeaderUICell.HEADER_ID_RATE -> {
                        vm.nextRateOrder()
                    }
                    I18nWordListHeaderUICell.HEADER_ID_UPDATED -> {
                        vm.nextUpdatedOrder()
                    }
                    I18nWordListHeaderUICell.HEADER_ID_LANG -> {

                    }
                }
            }
            .padding(vertical = 4.dp)
    )
}