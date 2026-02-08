package me.shouheng.i18n.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import easy_i18n.composeapp.generated.resources.Res
import easy_i18n.composeapp.generated.resources.tab_info_name
import me.shouheng.i18n.view.info.AppInfoCard
import me.shouheng.i18n.view.info.ContactsCard
import me.shouheng.i18n.view.info.DeveloperCard
import me.shouheng.i18n.view.info.LogoAndTitleView
import me.shouheng.i18n.view.info.RecommendCard
import me.shouheng.i18n.vm.InfoViewModel
import me.shouheng.i18n.widget.PageTitle
import me.shouheng.i18n.widget.PageToolbar
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/** 信息页面 */
@Preview
@Composable
fun InfoPage(vm: InfoViewModel) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column {
            Toolbar()
            PageBody(vm)
        }
    }
}

/** 页面主体内容 */
@Composable
private fun PageBody(vm: InfoViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 15.dp, vertical = 15.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LogoAndTitleView()
        AppInfoCard(vm)
        ContactsCard(vm)
        DeveloperCard(vm)
        RecommendCard(vm)
    }
}

/** 页面工具栏 */
@Composable
private fun Toolbar() {
    PageToolbar {
        PageTitle(
            stringResource(Res.string.tab_info_name),
            Icons.Default.Info,
            modifier = Modifier.weight(1f)
        )
    }
}
