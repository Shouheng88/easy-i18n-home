package me.shouheng.i18n.i18n.android

import me.shouheng.i18n.data.ErrorCode
import me.shouheng.i18n.data.common.Resource
import me.shouheng.i18n.data.model.I18nWordModel
import me.shouheng.i18n.i18n.IWordDeleter

/** Android strings.xml 单词删除逻辑 */
object AndroidWordDeleter: IWordDeleter {
    override fun delete(word: I18nWordModel): Resource<Unit> {
        for (meaning in word.meanings) {
            val origin = meaning.origin ?: continue
            val resource = origin as? AbsAndroidTextResource ?: continue
            // 执行删除操作
            val name = resource.getTextName()
            val result = AndroidXmlParser.delete(name, resource.file)
            // 删除失败：一个失败整体就是失败
            if (!result) {
                return Resource.failure(ErrorCode.DELETE_IO, "failed when write file")
            }
        }
        return Resource.success(Unit)
    }
}