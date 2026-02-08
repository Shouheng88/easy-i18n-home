package me.shouheng.i18n.view.i18n.wordlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import easy_i18n.composeapp.generated.resources.Res
import easy_i18n.composeapp.generated.resources.word_filter_lang
import easy_i18n.composeapp.generated.resources.word_filter_lang_select_all
import easy_i18n.composeapp.generated.resources.word_filter_lang_unselect_all
import me.shouheng.i18n.vm.I18nViewModel
import me.shouheng.i18n.widget.SmallIconButton
import org.jetbrains.compose.resources.stringResource

/** 多语言选择 */
@Composable
fun LanguageFilterView(vm: I18nViewModel) {
    var open by remember { mutableStateOf(false) }
    val ignored by vm.ignoreLanguages.collectAsState()
    val meanings = vm.pathContent.value?.words?.firstOrNull()?.meanings ?: emptyList()
    Box {
        SmallIconButton(Icons.Default.FilterList) {
            open = true
        }
        DropdownMenu(
            expanded = open,
            modifier = Modifier.fillMaxHeight(.8f),
            onDismissRequest = { open = false }
        ) {
            DropdownMenuItem(
                text = {
                    Text(stringResource(Res.string.word_filter_lang), fontWeight = FontWeight.Bold)
                },
                onClick = {}
            )
            Text(
                stringResource(Res.string.word_filter_lang_select_all),
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .clickable { vm.ignoreLanguages.value = emptyList() }
                    .fillMaxWidth()
                    .padding(vertical = 5.dp, horizontal = 15.dp)
            )
            Text(
                stringResource(Res.string.word_filter_lang_unselect_all),
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .clickable { vm.ignoreLanguages.value = meanings.map { it.language } }
                    .fillMaxWidth()
                    .padding(vertical = 5.dp, horizontal = 15.dp)
            )
            meanings.forEach {
                val language = it.language
                Row(
                    modifier = Modifier
                        .clickable {
                            val languages = ignored.toMutableList()
                            if (languages.contains(language)) {
                                languages.remove(language)
                            } else {
                                languages.add(language)
                            }
                            vm.ignoreLanguages.value = languages
                        }
                        .fillMaxWidth()
                        .padding(vertical = 5.dp, horizontal = 15.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val selected = !ignored.contains(language)
                    Icon(
                        if (selected) Icons.Default.CheckBox else Icons.Default.CheckBoxOutlineBlank,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(it.languageNameGetter.get(), fontSize = 13.sp)
                }
            }
        }
    }
}