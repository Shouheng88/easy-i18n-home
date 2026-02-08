package me.shouheng.i18n.view.info

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import easy_i18n.composeapp.generated.resources.Res
import easy_i18n.composeapp.generated.resources.info_developer_accounts
import me.shouheng.i18n.data.UIConst
import me.shouheng.i18n.vm.InfoViewModel
import me.shouheng.i18n.widget.SettingSectionCard
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/** 开发者信息 */
@Preview
@Composable
fun DeveloperCard(vm: InfoViewModel) {
    val developerAccounts by vm.developerAccounts.collectAsState()
    LaunchedEffect(Unit) {
        vm.getDeveloperAccounts()
    }
    SettingSectionCard(
        stringResource(Res.string.info_developer_accounts),
        modifier = Modifier.fillMaxWidth(UIConst.SETTING_ITEM_MAX_FRACTION)
    ) {
        developerAccounts.forEach {
            SocialAccountItemView(it)
        }
    }
}
