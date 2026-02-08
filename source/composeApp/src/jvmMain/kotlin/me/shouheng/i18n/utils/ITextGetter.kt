package me.shouheng.i18n.utils

import androidx.compose.runtime.Composable
import me.shouheng.i18n.I18nWord
import me.shouheng.i18n.data.model.I18nLanguage
import me.shouheng.i18n.manager.LanguageManager
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import java.text.SimpleDateFormat
import java.util.*

fun StringResource.asGetter() = StringResourceTextGetter(this)

fun String.asGetter() = StringTextGetter(this)

fun I18nWord?.asGetter() = UpdatedTimeTextGetter(this)

interface ITextGetter {
    @Composable fun get(): String
}

class StringTextGetter(val text: String): ITextGetter {
    @Composable
    override fun get(): String = text
}

class StringResourceTextGetter(val textRes: StringResource): ITextGetter {
    @Composable
    override fun get(): String = stringResource(textRes)
}

class StringResourceAndTextsTextGetter(
    val textRes: StringResource,
    vararg texts: String,
): ITextGetter {

    private val text: String = texts.toList().joinToString("")

    @Composable
    override fun get(): String = stringResource(textRes) + text
}

class TextConnector(
    vararg texts: ITextGetter
): ITextGetter {

    private val texts = texts.toList()

    @Composable
    override fun get(): String = texts.map { it.get() }.joinToString("")
}

class UpdatedTimeTextGetter(dto: I18nWord?): ITextGetter {

    private var text: String =""

    init {
        dto?.updatedTime?.let {
            val date = Date(it)
            text = yearDateFormat.format(date)
        }
    }

    @Composable
    override fun get(): String = text

    companion object Companion {
        private val yearDateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
    }
}

class LanguageNameGetter(
    val language: String
): ITextGetter {
    @Composable
    override fun get(): String {
        val language = LanguageManager.getLanguage(this.language)
        if (language == I18nLanguage.DEF) {
            return stringResource(language.nameRes)
        }
        if (language != null) {
            return stringResource(language.nameRes) + "(${this.language})"
        }
        return this.language
    }
}
