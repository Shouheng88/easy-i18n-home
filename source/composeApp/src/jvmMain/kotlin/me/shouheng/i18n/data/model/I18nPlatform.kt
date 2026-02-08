package me.shouheng.i18n.data.model

import easy_i18n.composeapp.generated.resources.Res
import easy_i18n.composeapp.generated.resources.ic_android
import easy_i18n.composeapp.generated.resources.ic_apple
import easy_i18n.composeapp.generated.resources.ic_compose
import easy_i18n.composeapp.generated.resources.ic_flutter
import easy_i18n.composeapp.generated.resources.ic_properties
import me.shouheng.i18n.i18n.android.AndroidKeywordLoader
import me.shouheng.i18n.i18n.IKeywordLoader
import me.shouheng.i18n.i18n.flutter.FlutterKeyLoader
import me.shouheng.i18n.i18n.ios.IOSKeywordLoader
import me.shouheng.i18n.i18n.java.PropertiesKeyLoader
import org.jetbrains.compose.resources.DrawableResource

enum class I18nPlatform(
    val id: Int,
    val displayName: String,
    val icon: DrawableResource,
) {
    Android(0, "Android", Res.drawable.ic_android),
    IOS(1, "iOS", Res.drawable.ic_apple),
    ComposeMultiplatform(2, "Compose Multiplatform", Res.drawable.ic_compose),
    JavaProperties(3, "Java Properties", Res.drawable.ic_properties),
    Flutter(4, "Flutter", Res.drawable.ic_flutter),
    ;

    /** 获取一个文件加载器 */
    fun newLoader(): IKeywordLoader {
        return when (this) {
            Android -> AndroidKeywordLoader()
            IOS -> IOSKeywordLoader()
            ComposeMultiplatform -> AndroidKeywordLoader()
            JavaProperties -> PropertiesKeyLoader()
            Flutter -> FlutterKeyLoader()
        }
    }

    companion object Companion {
        fun from(id: Int): I18nPlatform {
            return I18nPlatform.entries.firstOrNull { it.id == id } ?: Android
        }
    }
}