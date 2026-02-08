package me.shouheng.i18n.manager

import me.shouheng.i18n.data.model.I18nLanguage
import me.shouheng.i18n.data.model.I18nResourceType

/** 语言管理 */
object LanguageManager {

    private val androidMap: Map<String, I18nLanguage>
    private val iosMap: Map<String, I18nLanguage>
    private val propertiesMap: Map<String, I18nLanguage>
    private val flutterMap: Map<String, I18nLanguage>

    init {
        // android
        val android = mutableMapOf<String, I18nLanguage>()
        I18nLanguage.entries.forEach { language ->
            language.android.forEach { file ->
                android[file] = language
            }
        }
        androidMap = android

        // ios
        val ios = mutableMapOf<String, I18nLanguage>()
        I18nLanguage.entries.forEach { language ->
            language.ios.forEach { file ->
                ios[file] = language
            }
        }
        iosMap = ios

        // properties
        val properties = mutableMapOf<String, I18nLanguage>()
        I18nLanguage.entries.forEach { language ->
            language.properties.forEach { file ->
                properties[file] = language
            }
        }
        propertiesMap = properties

        // flutter arb
        val arb = mutableMapOf<String, I18nLanguage>()
        I18nLanguage.entries.forEach { language ->
            language.flutterArb.forEach { file ->
                arb[file] = language
            }
        }
        flutterMap = arb
    }

    /** 获取多语言 */
    fun getLanguage(
        from: String,
        type: I18nResourceType? = null
    ): I18nLanguage? {
        val language = parseLanguageFrom(from, type)
        return when(type) {
            I18nResourceType.AndroidXML, I18nResourceType.ComposeMultiplatformXML ->
                androidMap[from] ?: androidMap[language]
            I18nResourceType.IOSCStrings, I18nResourceType.IOSXCStrings ->
                iosMap[from] ?: iosMap[language]
            I18nResourceType.JavaProperties ->
                propertiesMap[from] ?: propertiesMap[language]
            I18nResourceType.FlutterArb ->
                flutterMap[from] ?: flutterMap[language]
            null -> androidMap[from]
                ?: iosMap[from]
                ?: androidMap[language]
                ?: iosMap[language]
                ?: propertiesMap[from]
                ?: propertiesMap[language]
                ?: flutterMap[from]
                ?: flutterMap[language] // 只要 Android 和 iOS 里面找一些，基本可以覆盖了
        }
    }

    /** 从包含地区的多语言中解析语言 */
    fun parseLanguageFrom(
        language: String,
        type: I18nResourceType?
    ): String {
        if (type == null) {
            return language
        }

        return when(type) {
            I18nResourceType.AndroidXML, I18nResourceType.ComposeMultiplatformXML -> {
                language.split('-').first()
            }
            I18nResourceType.IOSCStrings, I18nResourceType.IOSXCStrings -> {
                language.split('-').first()
            }
            I18nResourceType.JavaProperties -> {
                language.split('_').first()
            }
            I18nResourceType.FlutterArb -> {
                language.split('_').first()
            }
        }
    }
}