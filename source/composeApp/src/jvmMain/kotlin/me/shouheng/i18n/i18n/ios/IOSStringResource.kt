package me.shouheng.i18n.i18n.ios

import io.github.vinceglb.filekit.PlatformFile
import me.shouheng.i18n.data.model.XCStrings
import me.shouheng.i18n.i18n.AbsTextResource

/** iOS .strings 文件加载的词条 */
data class IOSStringResource(
    val origin: PlatformFile,
    val name: String,
    val value: String,
    /** 词条的名称是否被引号包裹 */
    val nameQuoted: Boolean,
): AbsTextResource(origin) {

    override fun getTextName(): String = name

    override fun getDisplayValue(): String = value
}

/** XCStrings 字符串资源 */
data class XCStringResource(
    val origin: PlatformFile,
    val name: String,
    val language: String,
    val xcStrings: XCStrings,
): AbsTextResource(origin) {

    /** 当前的字符串资源是否是我们支持的类型：由于 xcstrings 格式支持的太多，暂时不打算支持那么多 */
    private val isSupport: Boolean

    private val displayValue: String

    private val description: String?

    private val isPlural: Boolean

    private val sourceLanguage: String?

    init {
        val entry = xcStrings.strings?.get(name)
        val localization = entry?.localizations?.get(language)
        description = entry?.comment
        displayValue = localization?.value
            ?: localization?.stringUnit?.value
            ?: localization?.stringUnit?.pluralRules?.entries
                ?.joinToString("\n") { "- ${it.key}: ${it.value}" }
            ?: ""
        isSupport = localization?.isSupport() == true
        isPlural = localization?.stringUnit?.pluralRules != null
        sourceLanguage = xcStrings.sourceLanguage
    }

    fun getDescription(): String? = description

    override fun getTextName(): String = name

    override fun getDisplayValue(): String = displayValue

    override fun isSupport(): Boolean = isSupport

    override fun isPlural(): Boolean = isPlural

    override fun getSourceLanguage(): String? = sourceLanguage
}