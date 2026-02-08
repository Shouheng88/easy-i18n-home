package me.shouheng.i18n.net.translator.model

import kotlinx.serialization.Serializable

@Serializable
class GoogleTranslateData {

    var data: DataDTO? = null

    @Serializable
    class DataDTO {

        var translations: List<TranslationsDTO>? = null

        @Serializable
        class TranslationsDTO {

            var detectedSourceLanguage: String? = null

            var model: String? = null

            var translatedText: String? = null
        }
    }
}
