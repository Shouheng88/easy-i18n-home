package me.shouheng.i18n.i18n

import me.shouheng.i18n.data.common.Resource
import me.shouheng.i18n.data.model.I18nWordModel

/** 删除器，不同类型的文件删除逻辑不同 */
interface IWordDeleter {

    /** 删除单词 */
    fun delete(word: I18nWordModel): Resource<Unit>
}