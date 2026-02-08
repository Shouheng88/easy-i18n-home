package me.shouheng.i18n

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import easy_i18n.composeapp.generated.resources.Res
import easy_i18n.composeapp.generated.resources.app_name
import easy_i18n.composeapp.generated.resources.ic_logo
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

fun main() = application {
    PanGu.init()
    Window(
        onCloseRequest = ::exitApplication,
        title = stringResource(Res.string.app_name),
        icon = painterResource(Res.drawable.ic_logo)
    ) {
        App()
    }
}