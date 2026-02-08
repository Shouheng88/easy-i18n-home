package me.shouheng.i18n.data.model

data class XCStrings(
    var sourceLanguage: String? = null,
    var strings: Map<String, XCStringEntry>? = null,
    var version: String? = null,
)

data class XCStringEntry(
    var comment: String? = null,
    var localizations: Map<String, Localization>? = null,
    var metadata: Map<String, Any>? = null // 元数据（如是否已翻译）
)

data class Localization(
    var value: String? = null,
    var stringUnit: StringUnit? = null,
) {
    fun isSupport() = value != null || true == stringUnit?.isSupport()
}

data class StringUnit(
    var value: String? = null,
    var state: String? = null,
    // 复数规则（如 "one"、"other"）
    var pluralRules: Map<String, String>? = null
) {
    fun isSupport() = value != null || pluralRules != null
}