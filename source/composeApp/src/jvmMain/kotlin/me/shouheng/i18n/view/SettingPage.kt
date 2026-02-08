package me.shouheng.i18n.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import easy_i18n.composeapp.generated.resources.Res
import easy_i18n.composeapp.generated.resources.tab_setting_name
import me.shouheng.i18n.utils.isPossibleDebug
import me.shouheng.i18n.view.settings.DebugSettingCard
import me.shouheng.i18n.view.settings.GeneralSettingCard
import me.shouheng.i18n.view.settings.I18nSettingCard
import me.shouheng.i18n.widget.PageTitle
import me.shouheng.i18n.widget.PageToolbar
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/** 设置页面 */
@Preview
@Composable
fun SettingPage() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column {
            Toolbar()
            PageBody()
        }
    }
}

/** 页面主体内容 */
@Composable
private fun PageBody() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 15.dp, vertical = 15.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GeneralSettingCard()
        I18nSettingCard()
        if (isPossibleDebug()) {
            DebugSettingCard()
        }
    }
}

/** 页面工具栏 */
@Composable
private fun Toolbar() {
    PageToolbar {
        PageTitle(
            stringResource(Res.string.tab_setting_name),
            Icons.Default.Settings,
            modifier = Modifier.weight(1f)
        )
    }
}