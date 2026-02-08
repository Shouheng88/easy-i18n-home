package me.shouheng.i18n.data.model

import easy_i18n.composeapp.generated.resources.*
import me.shouheng.i18n.I18nPath
import me.shouheng.i18n.data.UIConst.WORD_LIST_DATE_CELL_WIDTH
import me.shouheng.i18n.data.UIConst.WORD_LIST_NO_CELL_WIDTH
import me.shouheng.i18n.data.UIConst.WORD_LIST_RATE_CELL_WIDTH
import me.shouheng.i18n.data.UIConst.WORD_LIST_TEXT_CELL_WIDTH
import me.shouheng.i18n.db.DB
import me.shouheng.i18n.manager.LanguageManager
import me.shouheng.i18n.utils.StringResourceAndTextsTextGetter
import me.shouheng.i18n.utils.asGetter
import me.shouheng.i18n.utils.extension.getResourceType

/** I18 词条分组：路径和对应的词条 */
class PathContent(
    /** 多语言路径 */
    val path: I18nPath,
    /** 该多语言下的词条 */
    val words: List<I18nWordModel>,
    query: WordQuery,
) {

    /** 表头 */
    val headers = mutableListOf<I18nWordListHeaderUICell>()

    /** 行 */
    val rows = mutableListOf<I18nWordListBodyUIRow>()

    init {
        // 注意保证 header 和 列表 的多语言顺序一致
        headers.add(I18nWordListHeaderUICell(
            I18nWordListHeaderUICell.HEADER_ID_NO,
            Res.string.table_column_name_no.asGetter(),
            WORD_LIST_NO_CELL_WIDTH,
            textCenter = true
        ))
        headers.add(I18nWordListHeaderUICell(
            I18nWordListHeaderUICell.HEADER_ID_NAME,
            StringResourceAndTextsTextGetter(
                Res.string.table_column_name_word,
                " ",
                if (query.keyword != null) "(*${query.keyword}*)" else "",
                query.nameOrder.displayText
            ),
            WORD_LIST_TEXT_CELL_WIDTH
        ))
        headers.add(I18nWordListHeaderUICell(
            I18nWordListHeaderUICell.HEADER_ID_RATE,
            StringResourceAndTextsTextGetter(Res.string.table_column_name_rate, " ", query.rateOrder.displayText),
            WORD_LIST_RATE_CELL_WIDTH,
            textCenter = true
        ))
        headers.add(I18nWordListHeaderUICell(
            I18nWordListHeaderUICell.HEADER_ID_DESC,
            Res.string.table_column_name_desc.asGetter(),
            WORD_LIST_TEXT_CELL_WIDTH
        ))
        (words.firstOrNull()?.meanings ?: emptyList()).forEach {
            headers.add(I18nWordListHeaderUICell(
                I18nWordListHeaderUICell.HEADER_ID_LANG,
                it.languageNameGetter,
                WORD_LIST_TEXT_CELL_WIDTH,
                language = it.language
            ))
        }
        headers.add(I18nWordListHeaderUICell(
            I18nWordListHeaderUICell.HEADER_ID_UPDATED,
            StringResourceAndTextsTextGetter(Res.string.table_column_name_updated, " ", query.updatedOrder.displayText),
            WORD_LIST_DATE_CELL_WIDTH
        ))

        // 单词 行
        words.forEachIndexed { index, word ->
            val items = mutableListOf<I18nWordListBodyUICell>()
            items.add(I18nWordListBodyUICell(
                I18nWordListHeaderUICell.HEADER_ID_NO,
                "${index + 1}".asGetter(),
                WORD_LIST_NO_CELL_WIDTH,
                word,
                textCenter = true,
                textSize = 12
            ))
            items.add(I18nWordListBodyUICell(
                I18nWordListHeaderUICell.HEADER_ID_NAME,
                word.name.asGetter(),
                WORD_LIST_TEXT_CELL_WIDTH,
                word
            ))
            items.add(I18nWordListBodyUICell(
                I18nWordListHeaderUICell.HEADER_ID_RATE,
                "".asGetter(),
                WORD_LIST_RATE_CELL_WIDTH,
                word,
                textCenter = true,
                icon = word.getPercentageIcon(),
                iconColor = word.getPercentageColor()
            ))
            items.add(I18nWordListBodyUICell(
                I18nWordListHeaderUICell.HEADER_ID_DESC,
                (word.description ?: "").asGetter(),
                WORD_LIST_TEXT_CELL_WIDTH,
                word
            ))
            word.meanings.forEach { meaning ->
                items.add(I18nWordListBodyUICell(
                    I18nWordListHeaderUICell.HEADER_ID_LANG,
                    (meaning.origin?.getDisplayValue() ?: "").asGetter(),
                    WORD_LIST_TEXT_CELL_WIDTH,
                    word,
                    meaning,
                    language = meaning.language
                ))
            }
            items.add(I18nWordListBodyUICell(
                I18nWordListHeaderUICell.HEADER_ID_UPDATED,
                word.dto.asGetter(),
                WORD_LIST_DATE_CELL_WIDTH,
                word
            ))
            rows.add(I18nWordListBodyUIRow(index, items, word))
        }
    }

    /** 计算需要翻译的词条的数量 */
    fun countWordsNeedTranslate(): Int {
        var count = 0
        words.forEach {
            count += it.countNeedTranslate()
        }
        return count
    }

    /** 获取翻译的原始语言 */
    fun getSourceLanguage(): I18nLanguage? {
        val newPath = DB.i18nPathDao.getById(path.id)
        val sourceLanguage = newPath?.sourceLanguage ?: path.sourceLanguage
        if (sourceLanguage != null) {
            val type = newPath.getResourceType()
            return LanguageManager.getLanguage(sourceLanguage, type)
        }
        return getAllLanguages().firstOrNull()
    }

    /** 单词是否包含原始语言 */
    fun hasWordSourceLanguage(): Boolean {
        return words.any { it.getSourceLanguage() != null }
    }

    /** 获取全部可选的语言 */
    fun getAllLanguages(): List<I18nLanguage> {
        return words.firstOrNull()?.meanings?.mapNotNull {
            LanguageManager.getLanguage(it.language)
        }?.toList() ?: emptyList()
    }
}
