package me.shouheng.i18n.i18n.ios

import me.shouheng.i18n.data.ErrorCode
import me.shouheng.i18n.data.common.Resource
import me.shouheng.i18n.data.model.I18nWordModel
import me.shouheng.i18n.data.model.SingleFileI18WordModel
import me.shouheng.i18n.data.model.WordUpdateItem
import me.shouheng.i18n.db.DB
import me.shouheng.i18n.i18n.IWordUpdater
import me.shouheng.i18n.utils.extension.loge

/** .strings 类型文件更新 */
object IOSDotStringWordUpdater: IWordUpdater {
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
            // 对值发生了变化的含义进行更新
            val meaning = it.meaning
            val value = it.value
            val succeed = DotStringsParser.update(word.order, word.name, value, meaning.file)
            if (!succeed) {
                loge { "更新词条失败！" }
                return Resource.failure(ErrorCode.UPDATE_WRITE, "写入失败！")
            }
        }
        return Resource.success(Unit)
    }
}

/** .xcstrings 类型文件更新 */
object IOSXCStringWordUpdater: IWordUpdater {
    override fun update(
        word: I18nWordModel,
        items: List<WordUpdateItem>,
        description: String
    ): Resource<Unit> {
        val model = word as? SingleFileI18WordModel
            ?: return Resource.failure(ErrorCode.UPDATE_TYPE_DIFF, "类型错误")

        val isDescriptionChanged = (word.description ?: "") != description
        val anyChanged = items.any { it.changed }
        if (anyChanged || isDescriptionChanged) {
            val succeed = XCStringsParser.update(model, description, items)
            if (!succeed) {
                loge { "更新词条失败！" }
                return Resource.failure(ErrorCode.UPDATE_WRITE, "写入失败！")
            }
        }

        return Resource.success(Unit)
    }
}