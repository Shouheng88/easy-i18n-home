package me.shouheng.i18n.i18n.flutter

import me.shouheng.i18n.data.ErrorCode
import me.shouheng.i18n.data.common.Resource
import me.shouheng.i18n.data.model.I18nWordModel
import me.shouheng.i18n.i18n.IWordDeleter

/** Flutter arb 单词删除逻辑 */
object FlutterArbDeleter: IWordDeleter {
    override fun delete(word: I18nWordModel): Resource<Unit> {
        for (meaning in word.meanings) {
            val origin = meaning.origin ?: continue
            val name = origin.getTextName()
            val result = FlutterArbParser.delete(name, meaning.file)
            if (!result) {
                return Resource.failure(ErrorCode.DELETE_IO, "failed when write file")
            }
        }
        return Resource.success(Unit)
    }
}