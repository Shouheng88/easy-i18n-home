package me.shouheng.i18n.data.model

import easy_i18n.composeapp.generated.resources.Res
import easy_i18n.composeapp.generated.resources.ic_android
import easy_i18n.composeapp.generated.resources.ic_apple
import easy_i18n.composeapp.generated.resources.ic_compose
import easy_i18n.composeapp.generated.resources.ic_flutter
import easy_i18n.composeapp.generated.resources.ic_json
import easy_i18n.composeapp.generated.resources.ic_properties
import easy_i18n.composeapp.generated.resources.ic_yaml
import easy_i18n.composeapp.generated.resources.platform_json
import easy_i18n.composeapp.generated.resources.platform_yaml
import me.shouheng.i18n.i18n.android.AndroidKeywordLoader
import me.shouheng.i18n.i18n.IKeywordLoader
import me.shouheng.i18n.i18n.flutter.FlutterKeyLoader
import me.shouheng.i18n.i18n.ios.IOSKeywordLoader
import me.shouheng.i18n.i18n.java.PropertiesKeyLoader
import me.shouheng.i18n.i18n.json.JsonKeyLoader
import me.shouheng.i18n.i18n.yaml.YamlKeyLoader
import me.shouheng.i18n.utils.ITextGetter
import me.shouheng.i18n.utils.asGetter
import org.jetbrains.compose.resources.DrawableResource

enum class I18nPlatform(
    val id: Int,
    val displayName: ITextGetter,
    val icon: DrawableResource,
) {
    Android(0, "Android".asGetter(), Res.drawable.ic_android),
    IOS(1, "iOS".asGetter(), Res.drawable.ic_apple),
    ComposeMultiplatform(2, "Compose Multiplatform".asGetter(), Res.drawable.ic_compose),
    JavaProperties(3, "Java Properties".asGetter(), Res.drawable.ic_properties),
    Flutter(4, "Flutter".asGetter(), Res.drawable.ic_flutter),
    JSON(5, Res.string.platform_json.asGetter(), Res.drawable.ic_json),
    YAML(6, Res.string.platform_yaml.asGetter(), Res.drawable.ic_yaml),
    ;

    /** 获取一个文件加载器 */
    fun newLoader(): IKeywordLoader {
        return when (this) {
            Android -> AndroidKeywordLoader()
            IOS -> IOSKeywordLoader()
            ComposeMultiplatform -> AndroidKeywordLoader()
            JavaProperties -> PropertiesKeyLoader()
            Flutter -> FlutterKeyLoader()
            JSON -> JsonKeyLoader()
            YAML -> YamlKeyLoader()
        }
    }

    companion object Companion {
        fun from(id: Int): I18nPlatform {
            return I18nPlatform.entries.firstOrNull { it.id == id } ?: Android
        }
    }
}