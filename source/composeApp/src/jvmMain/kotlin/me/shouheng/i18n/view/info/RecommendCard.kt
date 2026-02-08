package me.shouheng.i18n.view.info

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import easy_i18n.composeapp.generated.resources.Res
import easy_i18n.composeapp.generated.resources.info_recommend_apps
import me.shouheng.i18n.data.UIConst
import me.shouheng.i18n.vm.InfoViewModel
import me.shouheng.i18n.widget.SettingSectionCard
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/** 推荐应用信息 */
@Preview
@Composable
fun RecommendCard(vm: InfoViewModel) {
    val recommendApps by vm.recommendApps.collectAsState()
    LaunchedEffect(Unit) {
        vm.getRecommendApps()
    }
    SettingSectionCard(
        stringResource(Res.string.info_recommend_apps),
        modifier = Modifier.fillMaxWidth(UIConst.SETTING_ITEM_MAX_FRACTION)
    ) {
        recommendApps.forEach {
            RecommendAppItemView(it)
        }
    }
}