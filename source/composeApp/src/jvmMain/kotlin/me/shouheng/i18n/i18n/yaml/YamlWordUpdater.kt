package me.shouheng.i18n.i18n.yaml

import io.github.vinceglb.filekit.name
import me.shouheng.i18n.data.common.Resource
import me.shouheng.i18n.data.model.I18nWordModel
import me.shouheng.i18n.data.model.WordUpdateItem
import me.shouheng.i18n.db.DB
import me.shouheng.i18n.i18n.IWordUpdater

class YamlWordUpdater : IWordUpdater {

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

        items.forEach { item ->
            val success = YamlParser.update(word.name, item.value, item.meaning.file)
            if (!success) {
                return Resource.failure(code = "-1", message = "Failed to update ${word.name} in ${item.meaning.file.name}")
            }
        }
        return Resource.success(Unit)
    }
}
