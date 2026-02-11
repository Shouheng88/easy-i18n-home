package me.shouheng.i18n.data.model

/** 多语言资源类型 */
enum class I18nResourceType(
    val id: Int,
    val showEncoding: Boolean = false,
) {
    AndroidXML(0),
    IOSCStrings(1),
    IOSXCStrings(2),
    ComposeMultiplatformXML(3),
    JavaProperties(4, showEncoding = true),
    FlutterArb(5),
    JSON(6),
    YAML(7),
    ;

    fun simpleName(): String = when(this) {
        AndroidXML -> "xml"
        IOSCStrings -> "lproj"
        IOSXCStrings -> "xcstrings"
        ComposeMultiplatformXML -> "xml"
        JavaProperties -> "properties"
        FlutterArb -> "arb"
        JSON -> "json"
        YAML -> "yaml"
    }

    companion object {
        fun from(id: Int): I18nResourceType {
            return entries.first { it.id == id }
        }
    }
}