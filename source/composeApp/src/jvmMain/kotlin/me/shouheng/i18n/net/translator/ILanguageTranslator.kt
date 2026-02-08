package me.shouheng.i18n.net.translator

import me.shouheng.i18n.data.common.Resource
import me.shouheng.i18n.data.model.I18nLanguage
import me.shouheng.i18n.data.model.I18nResourceType

/** 翻译器 */
interface ILanguageTranslator {

    fun isConfigured(): Boolean

    /** 将制定的文本翻译成指定的语言 */
    suspend fun translate(
        resourceType: I18nResourceType,
        text: String,
        target: I18nLanguage,
        description: String,
        appInfo: String
    ): Resource<String>
}