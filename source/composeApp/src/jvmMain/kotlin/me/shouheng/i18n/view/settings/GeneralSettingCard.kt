package me.shouheng.i18n.view.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import easy_i18n.composeapp.generated.resources.*
import me.shouheng.i18n.data.Event
import me.shouheng.i18n.data.Router.ROUTE_SETTINGS
import me.shouheng.i18n.data.UIConst
import me.shouheng.i18n.data.common.AppLanguage
import me.shouheng.i18n.data.theme.AppTheme
import me.shouheng.i18n.manager.AppManger
import me.shouheng.i18n.manager.post
import me.shouheng.i18n.widget.SettingItemView
import me.shouheng.i18n.widget.SettingSectionCard
import org.jetbrains.compose.resources.stringResource

@Composable
fun GeneralSettingCard() {
    SettingSectionCard(
        stringResource(Res.string.set_general),
        Modifier.fillMaxWidth(UIConst.SETTING_ITEM_MAX_FRACTION)
    ) {
        SettingThemeItem()
        SettingLanguageItem()
    }
}

/** 应用的多语言 */
@Composable
private fun SettingLanguageItem() {
    var language by remember { mutableStateOf(AppManger.getAppLanguage()) }
    var showLanguagePicker by remember { mutableStateOf(false) }
    var showRestartDialog by remember { mutableStateOf(false) }
    Box {
        SettingItemView(
            Icons.Default.Language,
            stringResource(Res.string.set_app_language),
            stringResource(language.nameRes),
            "",
            onClick = { showLanguagePicker = true }
        )
        DropdownMenu(
            expanded = showLanguagePicker,
            onDismissRequest = { showLanguagePicker = false }
        ) {
            AppLanguage.entries.forEach { appLanguage ->
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                stringResource(appLanguage.nameRes),
                                fontSize = 15.sp,
                                modifier = Modifier.padding(vertical = 4.dp),
                            )
                        }
                    },
                    onClick = {
                        showLanguagePicker = false
                        language = appLanguage
                        AppManger.setAppLanguage(appLanguage)
                        showRestartDialog = true
                    }
                )
            }
        }
    }
    if (showRestartDialog) {
        AlertDialog(
            onDismissRequest = { showRestartDialog = false },
            title = { Text(stringResource(Res.string.set_app_language)) },
            text = { Text(stringResource(Res.string.set_app_language_tip)) },
            confirmButton = {
                TextButton(onClick = {
                    showRestartDialog = false
                    post(Event.EVENT_NAME_TO_PAGE, ROUTE_SETTINGS)
                }) {
                    Text(stringResource(Res.string.text_got_it))
                }
            },
        )
    }
}

/** 主题 */
@Composable
private fun SettingThemeItem() {
    var appTheme by remember { mutableStateOf(AppManger.getTheme()) }
    var showThemePicker by remember { mutableStateOf(false) }
    Box {
        SettingItemView(
            Icons.Default.ColorLens,
            stringResource(Res.string.set_app_theme),
            stringResource(appTheme.nameRes),
            "",
            onClick = { showThemePicker = true }
        )
        DropdownMenu(
            expanded = showThemePicker,
            onDismissRequest = { showThemePicker = false }
        ) {
            AppTheme.entries.forEach { theme ->
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(theme.icon, null, modifier = Modifier.size(20.dp))
                            Text(
                                stringResource(theme.nameRes),
                                fontSize = 15.sp,
                                modifier = Modifier.padding(vertical = 4.dp),
                            )
                        }
                    },
                    onClick = {
                        showThemePicker = false
                        appTheme = theme
                        AppManger.setTheme(theme)
                    }
                )
            }
        }
    }
}
