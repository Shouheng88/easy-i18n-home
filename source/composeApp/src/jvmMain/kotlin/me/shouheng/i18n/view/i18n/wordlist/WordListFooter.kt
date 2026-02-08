package me.shouheng.i18n.view.i18n.wordlist

import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import easy_i18n.composeapp.generated.resources.Res
import easy_i18n.composeapp.generated.resources.word_add_hint
import easy_i18n.composeapp.generated.resources.word_add_name_empty_tip
import easy_i18n.composeapp.generated.resources.word_add_title
import me.shouheng.i18n.manager.showError
import me.shouheng.i18n.view.i18n.AutoTranslateView
import me.shouheng.i18n.view.i18n.TranslateStateView
import me.shouheng.i18n.vm.I18nViewModel
import me.shouheng.i18n.widget.SimpleInputDialog
import org.jetbrains.compose.resources.stringResource

/** 表格底部-按钮 */
@Composable
fun WordListFooter(vm: I18nViewModel) {
    val translateState by vm.translateState.collectAsState()
    var showAddWordDialog by remember { mutableStateOf(false) }
    var showWordEditDialog by remember { mutableStateOf(false) }
    var wordToEdit by remember { mutableStateOf("") }
    Column {
        Row(
            modifier = Modifier.padding(vertical = 4.dp, horizontal = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 切换路径
            SwitchPathView(vm)
            Spacer(modifier = Modifier.weight(1f))
            // 翻译状态
            if (translateState != null) {
                TranslateStateView(translateState!!)
            }
            // 自动翻译
            AutoTranslateView(vm)
            // 搜索
            SearchKeywordView(vm)
            // 语言过滤
            LanguageFilterView(vm)
        }
        // 分割线
        HorizontalDivider(modifier = Modifier.height(0.5.dp).fillMaxWidth())
        // 目录状态
        PathStateView(vm)
    }

    // 添加单词对话框
    if (showAddWordDialog) {
        SimpleInputDialog(
            title = stringResource(Res.string.word_add_title),
            hint = stringResource(Res.string.word_add_hint),
            onDismiss = { showAddWordDialog = false },
            onConfirm = { name ->
                val name = name.trim()
                if (name.isEmpty()) {
                    showError(Res.string.word_add_name_empty_tip)
                    return@SimpleInputDialog
                }
                wordToEdit = name
                showWordEditDialog = true
            }
        )
    }
}
