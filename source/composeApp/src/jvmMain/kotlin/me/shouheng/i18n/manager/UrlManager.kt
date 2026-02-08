package me.shouheng.i18n.manager

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.text.AnnotatedString
import easy_i18n.composeapp.generated.resources.Res
import easy_i18n.composeapp.generated.resources.text_copied

/** URL 处理器 */
object UrlManager {

    /** 获取处理器 */
    val handler: UrlHandler
        @Composable get() = UrlHandler(
            LocalClipboardManager.current,
            LocalUriHandler.current
        )

    class UrlHandler(
        val clipboard: ClipboardManager,
        val uriHandler: UriHandler,
    ) {
        /** 处理账号链接 */
        fun handle(url: String) {
            if (url.startsWith("http://", true) || url.startsWith("https://", true)) {
                // 网络链接
                uriHandler.openUri(url)
            } else if (url.startsWith("mailto:")) {
                // 邮箱
                uriHandler.openUri(url)
            } else if (url.startsWith("i18n://copy/")) {
                val text = url.removePrefix("i18n://copy/")
                clipboard.setText(AnnotatedString(text))
                showSuccess(Res.string.text_copied)
            }
        }
    }
}