package me.shouheng.i18n.view.info

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import easy_i18n.composeapp.generated.resources.Res
import easy_i18n.composeapp.generated.resources.info_contact_us
import me.shouheng.i18n.data.UIConst
import me.shouheng.i18n.vm.InfoViewModel
import me.shouheng.i18n.widget.SettingSectionCard
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/** 类型方式 */
@Preview
@Composable
fun ContactsCard(vm: InfoViewModel) {
    val contacts by vm.contacts.collectAsState()
    LaunchedEffect(Unit) {
        vm.getAppAccounts()
    }
    SettingSectionCard(
        stringResource(Res.string.info_contact_us),
        modifier = Modifier.fillMaxWidth(UIConst.SETTING_ITEM_MAX_FRACTION)
    ) {
        contacts.forEach {
            SocialAccountItemView(it)
        }
    }
}
