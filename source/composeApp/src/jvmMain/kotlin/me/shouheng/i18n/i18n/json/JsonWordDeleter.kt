package me.shouheng.i18n.i18n.json

import me.shouheng.i18n.data.common.Resource
import me.shouheng.i18n.data.model.I18nWordModel
import me.shouheng.i18n.i18n.IWordDeleter
import io.github.vinceglb.filekit.*

class JsonWordDeleter : IWordDeleter {

    override fun delete(word: I18nWordModel): Resource<Unit> {
        word.meanings.forEach { meaning ->
            if (meaning.origin != null) {
                val success = JsonParser.delete(word.name, meaning.file)
                if (!success) {
                    return Resource.failure(code = "-1", message = "Failed to delete ${word.name} in ${meaning.file.name}")
                }
            }
        }
        return Resource.success(Unit)
    }
}
