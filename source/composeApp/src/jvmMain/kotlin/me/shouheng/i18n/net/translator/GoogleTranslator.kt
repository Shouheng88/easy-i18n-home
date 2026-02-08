package me.shouheng.i18n.net.translator

import easy_i18n.composeapp.generated.resources.Res
import easy_i18n.composeapp.generated.resources.ai_translate_result_empty
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import me.shouheng.i18n.data.common.Resource
import me.shouheng.i18n.data.model.I18nLanguage
import me.shouheng.i18n.data.model.I18nResourceType
import me.shouheng.i18n.data.model.TranslatorType
import me.shouheng.i18n.manager.TranslatorManager
import me.shouheng.i18n.net.translator.model.GoogleTranslateData
import me.shouheng.i18n.utils.settings
import org.jetbrains.compose.resources.getString

/** 谷歌翻译 */
object GoogleTranslator: AbsLanguageTranslator() {

    private const val KEY_GOOGLE_TRANSLATOR_API_KEY = "__google_translator_api_key__"

    /** 谷歌翻译 API Key */
    fun getAPIKey(): String = settings.getString(KEY_GOOGLE_TRANSLATOR_API_KEY, "")

    /** 谷歌翻译 API Key */
    fun setAPIKey(key: String) {
        settings.putString(KEY_GOOGLE_TRANSLATOR_API_KEY, key)
    }

    override fun isConfigured(): Boolean = getAPIKey().isNotEmpty()

    override suspend fun translate(
        resourceType: I18nResourceType,
        text: String,
        target: I18nLanguage,
        description: String,
        appInfo: String
    ): Resource<String> {
        try {
            val url = "https://translation.googleapis.com/language/translate/v2"
            val key = getAPIKey()
            val response = client.post(url) {
                contentType(ContentType.Application.FormUrlEncoded)
                setBody(FormDataContent(Parameters.build {
                    append("q", text)
                    append("target", target.googleEngine)
                    append("key", key)
                }))
            }
            if (response.status != HttpStatusCode.OK) {
                return Resource.failure("${response.status}", response.bodyAsText())
            }
            val data: GoogleTranslateData? = response.body()
            val translations = data?.data?.translations
            val translation = translations?.get(0)
            var translated = translation?.translatedText
                ?: return Resource.failure("-2", getString(Res.string.ai_translate_result_empty))
            translated = TranslatorManager.textImprovement(TranslatorType.GOOGLE, resourceType, translated)
            return Resource.success(translated)
        } catch (e: Exception) {
            return Resource.failure("-1", e.message)
        }
    }
}

suspend fun main() {
    val ret = GoogleTranslator.translate(I18nResourceType.AndroidXML, "hello", I18nLanguage.ZH, "", "")
    println(ret)
}
