package me.shouheng.i18n.data.model

/** 交给底层更新的数据类，用来描述值和多语言之间的关系 */
data class WordUpdateItem(
    val value: String,
    val meaning: I18nWordModel.Meaning,
    val changed: Boolean,
)