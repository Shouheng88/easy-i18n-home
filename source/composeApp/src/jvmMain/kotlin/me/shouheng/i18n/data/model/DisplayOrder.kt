package me.shouheng.i18n.data.model

/** 翻译比的展示顺序 */
enum class DisplayOrder (val displayText: String) {
    NONE(""),
    ORDER("↑"),
    REVERSED("↓"),
    ;

    fun next(): DisplayOrder = when (this) {
        NONE -> ORDER
        ORDER -> REVERSED
        REVERSED -> NONE
    }
}
