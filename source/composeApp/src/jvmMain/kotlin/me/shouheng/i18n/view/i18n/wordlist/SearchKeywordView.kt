package me.shouheng.i18n.view.i18n.wordlist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import easy_i18n.composeapp.generated.resources.Res
import easy_i18n.composeapp.generated.resources.word_search
import easy_i18n.composeapp.generated.resources.word_search_hint
import me.shouheng.i18n.vm.I18nViewModel
import me.shouheng.i18n.widget.SmallIconButton
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/** 关键字查询 */
@Composable
fun SearchKeywordView(vm: I18nViewModel) {
    var open by remember { mutableStateOf(false) }
    var keyword by remember { mutableStateOf("") }
    Box {
        SmallIconButton(Icons.Default.Search) {
            open = true
        }
        DropdownMenu(
            expanded = open,
            modifier = Modifier.padding(bottom = 15.dp),
            onDismissRequest = { open = false }
        ) {
            DropdownMenuItem(
                text = {
                    Text(stringResource(Res.string.word_search), fontWeight = FontWeight.Bold)
                },
                onClick = {}
            )
            OutlinedTextField(
                value = keyword,
                onValueChange = {
                    keyword = it
                    vm.search(keyword.ifEmpty { null })
                },
                label = { Text(stringResource(Res.string.word_search_hint)) },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp),
                singleLine = true
            )
        }
    }
}

@Preview
@Composable
private fun SearchKeywordViewPreview() {
    SearchKeywordView(I18nViewModel())
}
