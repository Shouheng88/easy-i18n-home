package me.shouheng.i18n.data.common

import easy_i18n.composeapp.generated.resources.Res
import easy_i18n.composeapp.generated.resources.set_app_language_en_US
import easy_i18n.composeapp.generated.resources.set_app_language_system
import easy_i18n.composeapp.generated.resources.set_app_language_zh_CN
import org.jetbrains.compose.resources.StringResource
import java.util.*

/** 应用的多语言 */
enum class AppLanguage(
    val id: Int,
    val nameRes: StringResource,
    val locale: Locale,
) {
    SYSTEM(0, Res.string.set_app_language_system, Locale.getDefault()),
    ENGLISH(1, Res.string.set_app_language_en_US, Locale.ENGLISH),
    CHINESE_SIMPLIFIED(2, Res.string.set_app_language_zh_CN, Locale.SIMPLIFIED_CHINESE),
    ;

    companion object {
        fun from(id: Int): AppLanguage = entries.firstOrNull { id == it.id } ?: SYSTEM
    }
}