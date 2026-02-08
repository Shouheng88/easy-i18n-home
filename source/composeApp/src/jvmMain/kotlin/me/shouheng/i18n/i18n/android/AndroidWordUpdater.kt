package me.shouheng.i18n.i18n.android

import me.shouheng.i18n.data.ErrorCode
import me.shouheng.i18n.data.common.Resource
import me.shouheng.i18n.data.model.I18nWordModel
import me.shouheng.i18n.data.model.WordUpdateItem
import me.shouheng.i18n.db.DB
import me.shouheng.i18n.i18n.IWordUpdater
import me.shouheng.i18n.utils.extension.loge

/** 更新 Android 词条 */
object AndroidWordUpdater: IWordUpdater {
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
        val origins = items.mapNotNull { it.meaning.origin }
        if (origins.isEmpty()) {
            loge { "无法确认数据类型：未找到类型" }
            return Resource.failure(ErrorCode.UPDATE_TYPE_MISS, "value type miss, empty")
        }
        val resource = origins.firstOrNull()
            ?: return Resource.failure(ErrorCode.UPDATE_TYPE_MISS, "value type miss, empty")
        val allSame = origins.all { it.javaClass == resource.javaClass }
        if (!allSame) {
            loge { "无法确认数据类型：类型不一致" }
            return Resource.failure(ErrorCode.UPDATE_TYPE_DIFF, "value type issue, conflict")
        }

        items.filter { it.changed }.forEach {
            // 对值发生了变化的含义进行更新
            val meaning = it.meaning
            val value = it.value
            // 如果存在原始的字符串就使用，否则使用其他语言的
            val origin = meaning.origin ?: resource
            val resource = AbsAndroidTextResource.from(origin, value, meaning.file)
            resource ?: loge { "获取 AbsAndroidTextResource 失败！" }
            resource ?: return Resource.failure(ErrorCode.UPDATE_RESOURCE_PARSE, "failed to get text resource")
            val succeed = AndroidXmlParser.update(resource, word.order, meaning.file)
            if (!succeed) {
                loge { "更新词条失败！" }
                return Resource.failure(ErrorCode.UPDATE_WRITE, "failed when write file")
            }
        }

        return Resource.success(Unit)
    }
}
