package me.shouheng.i18n.i18n.java

import me.shouheng.i18n.data.ErrorCode
import me.shouheng.i18n.data.common.Resource
import me.shouheng.i18n.data.model.I18nWordModel
import me.shouheng.i18n.data.model.WordUpdateItem
import me.shouheng.i18n.db.DB
import me.shouheng.i18n.i18n.IWordUpdater
import me.shouheng.i18n.utils.extension.loge

/** 更新 Properties 词条 */
object PropertiesWordUpdater: IWordUpdater {
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

        items.filter { it.changed }.forEach {
            val meaning = it.meaning
            val resource = PropertyResource(word.name, it.value, meaning.file)
            val succeed = PropertiesParser.update(resource, word.order, meaning.file, word.path.encoding)
            if (!succeed) {
                loge { "更新词条失败！" }
                return Resource.failure(ErrorCode.UPDATE_WRITE, "failed when write file")
            }
        }

        return Resource.success(Unit)
    }
}
