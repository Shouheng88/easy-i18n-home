package me.shouheng.i18n.data.model

import androidx.compose.runtime.Composable
import easy_i18n.composeapp.generated.resources.Res
import easy_i18n.composeapp.generated.resources.translator_name_google
import me.shouheng.i18n.net.translator.GoogleTranslator
import me.shouheng.i18n.net.translator.ILanguageTranslator
import me.shouheng.i18n.view.settings.dialog.translator.GoogleTranslatorConfigureView
import me.shouheng.i18n.vm.TranslatorSetterShareViewModel
import org.jetbrains.compose.resources.StringResource

enum class TranslatorType(
    val id: Int,
    val displayOrder: Int,
    val nameRes: StringResource,
    val proximateTimeCost: Long,
    val docUrl: String,
    val translator: ILanguageTranslator,
    val getConfigureView: @Composable (TranslatorSetterShareViewModel) -> Unit = { }
) {
    GOOGLE(0, 0, Res.string.translator_name_google, 2_000, "https://cloud.google.com/translate/docs", GoogleTranslator, { GoogleTranslatorConfigureView(it) }),
    ;

    /** 是否配置完成 */
    fun isConfigured(): Boolean = translator.isConfigured()

    companion object {
        fun from(id: Int): TranslatorType? {
            return TranslatorType.entries.firstOrNull { it.id == id }
        }

        val all = TranslatorType.entries.sortedBy { it.displayOrder }
    }
}