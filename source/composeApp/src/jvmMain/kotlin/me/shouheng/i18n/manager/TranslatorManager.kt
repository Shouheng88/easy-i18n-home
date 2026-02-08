package me.shouheng.i18n.manager

import easy_i18n.composeapp.generated.resources.Res
import easy_i18n.composeapp.generated.resources.translate_language_not_found
import easy_i18n.composeapp.generated.resources.translate_language_unclear
import easy_i18n.composeapp.generated.resources.translate_translator_type_miss
import me.shouheng.i18n.data.ErrorCode.TRANSLATOR_TARGET_LANGUAGE_DEFAULT
import me.shouheng.i18n.data.ErrorCode.TRANSLATOR_TARGET_LANGUAGE_NOT_FOUND
import me.shouheng.i18n.data.ErrorCode.TRANSLATOR_TYPE_NOT_FOUND
import me.shouheng.i18n.data.common.Resource
import me.shouheng.i18n.data.model.I18nLanguage
import me.shouheng.i18n.data.model.I18nResourceType
import me.shouheng.i18n.data.model.TranslatorType
import me.shouheng.i18n.utils.settings
import org.jetbrains.compose.resources.getString

/** 翻译 管理 */
object TranslatorManager {

    private const val KEY_TRANSLATOR_TYPE_ID = "__translator_type_id__"
    private const val KEY_TRANSLATE_DELAY_MILLISECONDS = "__translate_delay_milliseconds__"
    private const val KEY_XML_TAB_SPACE_COUNT = "__translator_xml_tab_space_count__"
    private const val KEY_ENABLE_TRANSLATE_IMPROVEMENT = "_translator_result_improvement__"

    private const val DEFAULT_TRANSLATE_DELAY_MILLISECONDS = 1500L

    /** XML Tab 空格数 */
    fun getXMLTabSpaceCount(): Int = settings.getInt(KEY_XML_TAB_SPACE_COUNT, 4)

    /** XML Tab 空格数 */
    fun getXMLTabSpace(): String {
        val count = getXMLTabSpaceCount()
        return if (count == 2) "  " else "    "
    }

    /** XML Tab 空格数 */
    fun setXMLTabSpaceCount(count: Int) {
        settings.putInt(KEY_XML_TAB_SPACE_COUNT, count)
    }

    /** 获取当前的翻译器的类型 */
    fun getTranslatorType(): TranslatorType? {
        val id = settings.getIntOrNull(KEY_TRANSLATOR_TYPE_ID) ?: return null
        return TranslatorType.from(id)
    }

    /** 设置翻译器的类型 */
    fun setTranslatorType(type: TranslatorType) {
        settings.putInt(KEY_TRANSLATOR_TYPE_ID, type.id)
    }

    /** 翻译延时时间 */
    fun getTranslateDelay(): Long = settings.getLong(KEY_TRANSLATE_DELAY_MILLISECONDS, DEFAULT_TRANSLATE_DELAY_MILLISECONDS)

    /** 翻译延时时间 */
    fun setTranslateDelay(delay: Long) {
        settings.putLong(KEY_TRANSLATE_DELAY_MILLISECONDS, delay)
    }

    /** 估算翻译的耗时, ms */
    fun evaluateTimeCost(count: Int): Long {
        if (count == 0) return 0
        val delay = getTranslateDelay()
        val type = getTranslatorType() ?: TranslatorType.GOOGLE
        return (count - 1) * delay + count * type.proximateTimeCost
    }

    /** 翻译结果优化 */
    fun setTranslatorImprovement(enable: Boolean) {
        settings.putBoolean(KEY_ENABLE_TRANSLATE_IMPROVEMENT, enable)
    }

    /** 是否启用结果优化 */
    fun isTranslatorImprovementEnabled() = settings.getBoolean(KEY_ENABLE_TRANSLATE_IMPROVEMENT, true)

    /** 文本优化 */
    fun textImprovement(
        translatorType: TranslatorType,
        resourceType: I18nResourceType,
        text: String
    ): String {
        if (!isTranslatorImprovementEnabled()) {
            return text
        }
        return when (resourceType) {
            I18nResourceType.AndroidXML -> {
                if (text.contains("<![CDATA[")) {
                    return text.replace("]]&gt;", "]]>")
                }
                text.replace("&", "&amp;")
                    .replace(">", "&gt;")
                    .replace("<", "&lt;")
                    .replace("…", "&#8230;")
                    .replace("...", "&#8230;")
                    .replace("]]&gt;", "]]>")
                    .replace("&#39;", "\\'")
            }
            I18nResourceType.IOSCStrings -> text
            I18nResourceType.IOSXCStrings ->
                // XML/HTML 格式的转义字符转换为普通字符
                text.replace("&quot;", "\"")
                    .replace("&#39;", "\'")
                    .replace("&amp;", "&")
                    .replace("&gt;", ">")
                    .replace("&lt;", "<")
                    .replace("&#8230;", "...")
            I18nResourceType.ComposeMultiplatformXML -> {
                text.replace("&", "&amp;")
                    .replace(">", "&gt;")
                    .replace("<", "&lt;")
                    .replace("…", "&#8230;")
                    .replace("...", "&#8230;")
                    .replace("]]&gt;", "]]>")
                    .replace("&#39;", "\\'")
            }
            I18nResourceType.JavaProperties ->
                // XML/HTML 格式的转义字符转换为普通字符
                text.replace("&quot;", "\"")
                    .replace("&#39;", "\'")
                    .replace("&amp;", "&")
                    .replace("&gt;", ">")
                    .replace("&lt;", "<")
                    .replace("&#8230;", "...")
            I18nResourceType.FlutterArb ->
                // XML/HTML 格式的转义字符转换为普通字符
                text.replace("&quot;", "\"")
                    .replace("&#39;", "\'")
                    .replace("&amp;", "&")
                    .replace("&gt;", ">")
                    .replace("&lt;", "<")
                    .replace("&#8230;", "...")
        }
    }

    /** 翻译指定的文本为指定的语言类型 */
    suspend fun translate(
        resourceType: I18nResourceType,
        from: String,
        target: String,
        description: String,
        appInfo: String
    ): Resource<String> {
        val type = getTranslatorType()
            ?: return Resource.failure(TRANSLATOR_TYPE_NOT_FOUND, getString(Res.string.translate_translator_type_miss))
        val language = LanguageManager.getLanguage(target, resourceType)
            ?: return Resource.failure(TRANSLATOR_TARGET_LANGUAGE_NOT_FOUND, getString(Res.string.translate_language_not_found))
        if (language == I18nLanguage.DEF || language == I18nLanguage.UNKNOWN) {
            return Resource.failure(TRANSLATOR_TARGET_LANGUAGE_DEFAULT, getString(Res.string.translate_language_unclear))
        }
        return type.translator.translate(resourceType, from, language, description, appInfo)
    }
}