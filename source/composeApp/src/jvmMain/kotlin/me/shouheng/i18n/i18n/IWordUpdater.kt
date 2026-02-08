package me.shouheng.i18n.i18n

import me.shouheng.i18n.data.common.Resource
import me.shouheng.i18n.data.model.I18nWordModel
import me.shouheng.i18n.data.model.WordUpdateItem

interface IWordUpdater {

    fun update(
        word: I18nWordModel,
        items: List<WordUpdateItem>,
        description: String
    ): Resource<Unit>
}
