package me.shouheng.i18n.i18n.ios

import me.shouheng.i18n.data.ErrorCode
import me.shouheng.i18n.data.common.Resource
import me.shouheng.i18n.data.model.I18nWordModel
import me.shouheng.i18n.data.model.SingleFileI18WordModel
import me.shouheng.i18n.i18n.IWordDeleter

/** iOS .strings 单词删除逻辑 */
object IOSDotStringWordDeleter: IWordDeleter {
    override fun delete(word: I18nWordModel): Resource<Unit> {
        for (meaning in word.meanings) {
            val origin = meaning.origin ?: continue
            // 执行删除操作
            val name = origin.getTextName()
            val succeed = DotStringsParser.delete(name, origin.file)
            // 删除失败：一个失败整体就是失败
            if (!succeed) {
                return Resource.failure(ErrorCode.DELETE_IO, "读写过程失败")
            }
        }
        return Resource.success(Unit)
    }
}

/** iOS .xcstrings 单词删除逻辑 */
object IOSXCStringWordDeleter: IWordDeleter {
    override fun delete(word: I18nWordModel): Resource<Unit> {
        val model = word as? SingleFileI18WordModel
            ?: return Resource.failure(ErrorCode.DELETE_TYPE_DIFF, "类型错误")
        val succeed = XCStringsParser.delete(model.name, model.origin.file)
        if (!succeed) {
            return Resource.failure(ErrorCode.DELETE_IO, "读写过程失败")
        }
        return Resource.success(Unit)
    }
}