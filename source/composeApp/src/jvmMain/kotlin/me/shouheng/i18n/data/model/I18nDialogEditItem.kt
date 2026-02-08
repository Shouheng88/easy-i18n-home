package me.shouheng.i18n.data.model

import me.shouheng.i18n.utils.ITextGetter

/** 词条编辑对话框的编辑器 */
data class I18nDialogEditItem(
    var hintGetter: ITextGetter,
    var value: String,
    val meaning: I18nWordModel.Meaning,
    var isError: Boolean = false,
) {

    /** 词条的值是否发生了变化 */
    fun isValueChanged(): Boolean {
        val old = meaning.origin?.getDisplayValue() ?: ""
        return old != value
    }

    /** 转变为 WordUpdateItem */
    fun toWordUpdateItem() = WordUpdateItem(this.value, this.meaning, this.isValueChanged())

    companion object {
        fun from(meaning: I18nWordModel.Meaning): I18nDialogEditItem {
            return I18nDialogEditItem(
                meaning.languageNameGetter,
                meaning.origin?.getDisplayValue() ?: "",
                meaning
            )
        }
    }
}