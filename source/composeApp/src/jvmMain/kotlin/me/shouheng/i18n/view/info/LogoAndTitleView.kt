package me.shouheng.i18n.view.info

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import easy_i18n.composeapp.generated.resources.Res
import easy_i18n.composeapp.generated.resources.app_name
import easy_i18n.composeapp.generated.resources.ic_logo
import easy_i18n.composeapp.generated.resources.info_slogan
import io.kamel.core.Resource
import io.kamel.image.KamelImage
import me.shouheng.i18n.utils.AppUtils
import me.shouheng.i18n.view.info.dialog.AppInfoDialog
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/** logo 和标题 */
@Preview
@Composable
fun LogoAndTitleView() {
    var showAppInfoDialog by remember { mutableStateOf(false) }
    var lastVersionClick by remember { mutableStateOf(0L) }
    var clickCount by remember { mutableStateOf(0) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        KamelImage(
            resource = { Resource.Success(painterResource(Res.drawable.ic_logo)) },
            contentDescription = null,
            modifier = Modifier.size(120.dp)//.border(1.dp, Colors.outline, CircleShape).clip(CircleShape)
        )
        Text(
            stringResource(Res.string.app_name),
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            modifier = Modifier
                .padding(top = 20.dp)
                .padding(horizontal = 80.dp)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = {
                            showAppInfoDialog = true
                        }
                    )
                }
        )
        Text(
            AppUtils.getAppVersionName(),
            fontSize = 12.sp,
        )
        Text(
            stringResource(Res.string.info_slogan),
            fontSize = 14.sp,
            modifier = Modifier
                .padding(top = 4.dp)
                .padding(horizontal = 40.dp)
        )
    }

    if (showAppInfoDialog) {
        AppInfoDialog {
            showAppInfoDialog = false
        }
    }
}