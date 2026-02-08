package me.shouheng.i18n.i18n.android

import io.github.vinceglb.filekit.PlatformFile
import me.shouheng.i18n.i18n.AbsTextResource
import me.shouheng.i18n.manager.TextManager
import me.shouheng.i18n.manager.TranslatorManager

/** Android 字符串资源文件内容 */
data class AndroidResource(
    val namespaces: List<String>,
    val texts: List<AbsAndroidTextResource>,
)

/** Android 多语言资源的抽象类 */
abstract class AbsAndroidTextResource(
    file: PlatformFile,
    private val attributes: Map<String, String>,
): AbsTextResource(file) {

    abstract fun getXmlNode(): String

    override fun isTranslatable(): Boolean = attributes["translatable"] != "false"

    companion object {
        /**
         * 从原来的词条资源中获取一个新的资源
         *
         * @param origin 原始的词条样本，用来获取原词条的部分信息，但是并非原始的词条，因为可能存在某个词条对应的多语言不存在的情况
         * @param value 修改之后的值
         * @param file 该词条对应的多语言文件
         */
        fun from(
            origin: AbsTextResource,
            value: String,
            file: PlatformFile,
        ): AbsAndroidTextResource? {
            if (origin is StringResource) {
                return StringResource(file, origin.name, value, origin.isCData,  origin.attributes)
            }
            if (origin is PluralResource) {
                val items = TextManager.parsePlural(value) ?: return null
                return PluralResource(file, origin.name, items, origin.attributes)
            }
            if (origin is StringArrayResource) {
                val items = TextManager.parseArray(value) ?: return null
                return StringArrayResource(file, origin.name, items, origin.attributes)
            }
            return null
        }
    }
}

/** string 标签结果 */
data class StringResource(
    val origin: PlatformFile,
    val name: String,
    val value: String,
    val isCData: Boolean = false,
    val attributes: Map<String, String>,
): AbsAndroidTextResource(origin, attributes) {

    override fun getTextName(): String = name

    override fun getDisplayValue(): String = value

    override fun getXmlNode(): String {
        val tabSpace = TranslatorManager.getXMLTabSpace()
        val attributesText = attributes.entries.joinToString(" ") {
            "${it.key}=\"${it.value}\""
        }
        return "${tabSpace}<string $attributesText>$value</string>"
    }
}

/** plurals 标签结果 */
data class PluralResource(
    val origin: PlatformFile,
    val name: String,
    /** key: quantity (zero/one/other), value: text */
    val items: Map<String, String>,
    val attributes: Map<String, String>,
): AbsAndroidTextResource(origin, attributes) {

    override fun getTextName(): String = name

    override fun getDisplayValue(): String =
        items.entries.joinToString("\n") {
            "- ${it.key}: ${it.value}"
        }

    override fun getXmlNode(): String {
        val tabSpace = TranslatorManager.getXMLTabSpace()
        val attributesText = attributes.entries.joinToString(" ") {
            "${it.key}=\"${it.value}\""
        }
        return """${tabSpace}<plurals $attributesText>
${items.entries.joinToString("\n") {
    "${tabSpace}${tabSpace}<item quantity=\"${it.key}\">${it.value}</item>"
}}     
${tabSpace}</plurals>"""
    }

    override fun isPlural(): Boolean = true
}

/** string-array 标签结果 */
data class StringArrayResource(
    val origin: PlatformFile,
    val name: String,
    val items: List<String>,
    val attributes: Map<String, String>,
): AbsAndroidTextResource(origin, attributes) {

    override fun getTextName(): String = name

    override fun getDisplayValue(): String = items.joinToString("\n") { "- $it" }

    override fun getXmlNode(): String {
        val tabSpace = TranslatorManager.getXMLTabSpace()
        val attributesText = attributes.entries.joinToString(" ") {
            "${it.key}=\"${it.value}\""
        }
        return """${tabSpace}<string-array $attributesText>
${items.joinToString("\n") {
    "${tabSpace}${tabSpace}<item>${it}</item>"
}}
${tabSpace}</string-array>"""
    }

    override fun isArray(): Boolean = true
}