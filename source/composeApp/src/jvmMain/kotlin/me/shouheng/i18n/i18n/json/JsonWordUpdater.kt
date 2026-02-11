package me.shouheng.i18n.i18n.json

import me.shouheng.i18n.data.common.Resource
import me.shouheng.i18n.data.model.I18nWordModel
import me.shouheng.i18n.data.model.WordUpdateItem
import me.shouheng.i18n.i18n.IWordUpdater
import io.github.vinceglb.filekit.*
import me.shouheng.i18n.db.DB

class JsonWordUpdater : IWordUpdater {

    override fun update(
        word: I18nWordModel,
        items: List<WordUpdateItem>,
        description: String
    ): Resource<Unit> {
        val isDescriptionChanged = (word.description ?: "") != description
        if (isDescriptionChanged) {
            // 数据库更新
            DB.i18nWordDao.update(word, description)
        }

        // Update each item (each language)
        items.forEach { item ->
            // If the item has a value or we want to update it
            val success = JsonParser.update(word.name, item.value, item.meaning.file)
            if (!success) {
                return Resource.failure(code = "-1", message = "Failed to update ${word.name} in ${item.meaning.file.name}")
            }
        }
        return Resource.success(Unit)
    }
}
