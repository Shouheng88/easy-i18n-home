package me.shouheng.i18n.data.model

import androidx.compose.ui.graphics.Color
import me.shouheng.i18n.utils.ITextGetter
import org.jetbrains.compose.resources.DrawableResource

/** 多语言：单词列表 表头 */
data class I18nWordListHeaderUICell(
    val type: Int,
    /** 名称 */
    val nameGetter: ITextGetter,
    /** 宽度 */
    val width: Int,
    /** 文本居中 */
    val textCenter: Boolean = false,
    val language: String? = null,
) {
    companion object {
        const val HEADER_ID_NO = 0
        const val HEADER_ID_NAME = 1
        const val HEADER_ID_RATE = 2
        const val HEADER_ID_DESC = 3
        const val HEADER_ID_LANG = 4
        const val HEADER_ID_UPDATED = 5
    }
}

/** 多语言：单词列表 行 */
data class I18nWordListBodyUIRow(
    val index: Int,
    val items: List<I18nWordListBodyUICell>,
    val word: I18nWordModel,
) {
    /** 获取编辑条目 */
    fun getEditItems(ignored: List<String>): List<I18nDialogEditItem> {
        return this.items.filter {
            it.meaning != null && !ignored.contains(it.language)
        }.map {
            I18nDialogEditItem.from(it.meaning!!)
        }
    }
}

/** 多语言：单词列表 行内的格子 */
data class I18nWordListBodyUICell(
    val type: Int,
    /** 列表中展示的文案 */
    val textGetter: ITextGetter,
    /** 宽度 */
    val width: Int,
    /** 单词 */
    val word: I18nWordModel,
    /** 单词的含义 */
    val meaning: I18nWordModel.Meaning? = null,
    /** 文本居中 */
    val textCenter: Boolean = false,
    /** 文字大小 */
    val textSize: Int = 13,
    /** 图标 */
    val icon: DrawableResource? = null,
    /** 图标的颜色 */
    val iconColor: Color? = null,
    val language: String? = null
)