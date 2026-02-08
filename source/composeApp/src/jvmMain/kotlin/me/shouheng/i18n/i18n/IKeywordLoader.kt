package me.shouheng.i18n.i18n

import me.shouheng.i18n.I18nPath
import me.shouheng.i18n.data.model.I18nWordModel

interface IKeywordLoader {

    fun load(path: I18nPath): List<I18nWordModel>
}