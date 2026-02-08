package me.shouheng.i18n.i18n.java

import me.shouheng.i18n.data.ErrorCode
import me.shouheng.i18n.data.common.Resource
import me.shouheng.i18n.data.model.I18nWordModel
import me.shouheng.i18n.i18n.IWordDeleter

/** Java properties 单词删除逻辑 */
object PropertiesWordDeleter: IWordDeleter {
    override fun delete(word: I18nWordModel): Resource<Unit> {
        for (meaning in word.meanings) {
            val origin = meaning.origin ?: continue
            val name = origin.getTextName()
            val result = PropertiesParser.delete(name, meaning.file, word.path.encoding)
            if (!result) {
                return Resource.failure(ErrorCode.DELETE_IO, "failed when write file")
            }
        }
        return Resource.success(Unit)
    }
}