package me.shouheng.i18n.data.model

import androidx.compose.runtime.Composable
import easy_i18n.composeapp.generated.resources.*
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

/**
 * 多语言的映射关系
 *
 * 参考文档：
 * - 百度接口文档: https://ai.baidu.com/ai-doc/MT/4kqryjku9
 * - 谷歌接口文档: https://cloud.google.com/translate/docs/languages?hl=zh-cn
 * - Android 文档: https://developer.android.com/guide/topics/resources/providing-resources?hl=zh-cn#AlternativeResources
 * - ISO-639-1 语言码标准: https://www.loc.gov/standards/iso639-2/php/code_list.php
 * - 区域码 ISO 3166-1-alpha-2: https://www.iso.org/obp/ui/#iso:pub:PUB500001:en
 * - Libre 语言文档：https://libretranslate.com/languages
 * - 腾讯语言文档：https://cloud.tencent.com/document/api/551/15619
 */
enum class I18nLanguage(
    /** 语言名称：支持多语言 */
    val nameRes: StringResource,
    /** 该语言在 Android 下的名称 */
    val android: List<String>,
    /** 该语言在 iOS 下的名称 */
    val ios: List<String>,
    /** Java Properties 文件的规则 */
    val properties: List<String>,
    /** FlutterArb */
    val flutterArb: List<String>,
    /** Json */
    val json: List<String>,
    /** 谷歌翻译使用的引擎 */
    val googleEngine: String,
    /** 百度翻译使用的引擎 */
    val baiduEngine: String,
    /** Libre 使用的引擎 */
    val libreEngine: String,
    /** Tencent 使用的引擎 */
    val tencentEngine: String,
    /** DeepL 使用的引擎 */
    val deepLEngine: String,
) {
    // 未支持的类型
    DEF(
        Res.string.language_default,
        listOf(""),
        listOf(""),
        listOf(""),
        listOf(""),
        listOf(""),
        "", "", "", "", ""
    ),
    UNKNOWN(
        Res.string.language_unknown,
        listOf(),
        listOf(),
        listOf(),
        listOf(),
        listOf(),
        "", "", "", "", ""
    ),
    // 阿拉伯语
    AR(
        Res.string.language_ar,
        listOf("ar", "ar-rIL", "ar-rEG"),
        listOf("ar"),
        listOf("ar", "ar_IL", "ar_EG"),
        listOf("ar", "ar_IL", "ar_EG"),
        listOf("ar", "ar_IL", "ar-IL", "ar_EG", "ar-EG"),
        "ar", "ara", "ar", "ar", "AR"
    ),
    // 保加利亚语
    BG(
        Res.string.language_bg,
        listOf("bg", "bg-rBG"),
        listOf("bg"),
        listOf("bg", "bg_BG"),
        listOf("bg", "bg_BG"),
        listOf("bg", "bg_BG", "bg-BG"),
        "bg", "bul", "bg", "", "BG"
    ),
    // 孟加拉语
    BN(
        Res.string.language_bn,
        listOf("bn", "bn-rIN", "bn-rBD"),
        listOf("bn"),
        listOf("bn", "bn_IN", "bn_BD"),
        listOf("bn", "bn_IN", "bn_BD"),
        listOf("bn", "bn_IN", "bn-IN", "bn_BD", "bn-BD"),
        "bn", "ben", "bn", "", ""
    ),
    // 捷克语
    CS(
        Res.string.language_cs,
        listOf("cs", "cs-rCZ"),
        listOf("cs"),
        listOf("cs", "cs_CZ"),
        listOf("cs", "cs_CZ"),
        listOf("cs", "cs_CZ", "cs-CZ"),
        "cs", "cs", "cs", "", "CS"
    ),
    // 丹麦语
    DA(
        Res.string.language_da,
        listOf("da", "da-rDK"),
        listOf("da"),
        listOf("da", "da_DK"),
        listOf("da", "da_DK"),
        listOf("da", "da_DK", "da-DK"),
        "da", "dan", "da", "", "DA"
    ),
    // 德语
    DE(
        Res.string.language_de,
        listOf("de", "de-rDE", "de-rLI", "de-rAT", "de-rCH"),
        listOf("de"),
        listOf("de", "de_DE", "de_LI", "de_AT", "de_CH"),
        listOf("de", "de_DE", "de_LI", "de_AT", "de_CH"),
        listOf("de", "de_DE", "de-DE", "de_LI", "de-LI", "de_AT", "de-AT", "de_CH", "de-CH"),
        "de", "de", "de", "de", "DE"
    ),
    // 希腊语
    EL(
        Res.string.language_el,
        listOf("el", "el-rGR", "el-rCY"),
        listOf("el"),
        listOf("el", "el_GR", "el_CY"),
        listOf("el", "el_GR", "el_CY"),
        listOf("el", "el_GR", "el-GR", "el_CY", "el-CY"),
        "el", "el", "el", "", "EL"
    ),
    // 英语
    EN(
        Res.string.language_en,
        listOf("en", "en-rUS", "en-rAU", "en-rCA", "en-rIN", "en-rIE", "en-rNZ", "en-rSG", "en-rZA", "en-rGB"),
        listOf("en", "en-AU", "en-IN", "en-GB"),
        listOf("en", "en_US", "en_AU", "en_CA", "en_IN", "en_IE", "en_NZ", "en_SG", "en_ZA", "en_GB"),
        listOf("en", "en_US", "en_AU", "en_CA", "en_IN", "en_IE", "en_NZ", "en_SG", "en_ZA", "en_GB"),
        listOf("en", "en_US", "en-US", "en_AU", "en-AU", "en_CA", "en-CA", "en_IN", "en-IN", "en_IE", "en-IE", "en_NZ", "en-NZ", "en_SG", "en-SG", "en_ZA", "en-ZA", "en_GB", "en-GB"),
        "en", "en", "en", "en", "EN-US"
    ),
    // 西班牙语
    ES(
        Res.string.language_es,
        listOf("es", "es-rES"),
        listOf("es"),
        listOf("es", "es_ES"),
        listOf("es", "es_ES"),
        listOf("es", "es_ES", "es-ES"),
        "es", "spa", "es", "es", "ES"
    ),
    // 爱沙尼亚语
    ET(
        Res.string.language_et,
        listOf("et", "et-rEE"),
        listOf("et"),
        listOf("et", "et_EE"),
        listOf("et", "et_EE"),
        listOf("et", "et_EE", "et-EE"),
        "et", "est", "et", "", "ET"
    ),
    // 法语
    FR(
        Res.string.language_fr,
        listOf("fr"),
        listOf("fr"),
        listOf("fr"),
        listOf("fr"),
        listOf("fr"),
        "fr", "fra", "fr", "fr", "FR"
    ),
    FR_FR(
        Res.string.language_fr_fr,
        listOf("fr-rFR"),
        listOf("fr-FR"),
        listOf("fr_FR"),
        listOf("fr_FR"),
        listOf("fr_FR", "fr-FR"),
        "fr-FR", "fra", "fr", "fr", "FR"
    ),
    FR_CA(
        Res.string.language_fr_ca,
        listOf("fr-rCA"),
        listOf("fr-CA"),
        listOf("fr_CA"),
        listOf("fr_CA"),
        listOf("fr_CA", "fr-CA"),
        "fr-CA", "frn", "fr", "fr", "FR"
    ),
    // 芬兰语
    FI(
        Res.string.language_fi,
        listOf("fi", "fi-rFI"),
        listOf("fi"),
        listOf("fi", "fi_FI"),
        listOf("fi", "fi_FI"),
        listOf("fi", "fi_FI", "fi-FI"),
        "fi", "fin", "fi", "", "FI"
    ),
    // 印地语
    HI(
        Res.string.language_hi,
        listOf("hi"),
        listOf("hi"),
        listOf("hi"),
        listOf("hi"),
        listOf("hi"),
        "hi", "hi", "hi", "hi", ""
    ),
    // 匈牙利语
    HU(
        Res.string.language_hu,
        listOf("hu", "hu-rHU"),
        listOf("hu"),
        listOf("hu", "hu_HU"),
        listOf("hu", "hu_HU"),
        listOf("hu", "hu_HU", "hu-HU"),
        "hu", "hu", "hu", "", "HU"
    ),
    // 印尼语
    IN(
        Res.string.language_in,
        listOf("in"),
        listOf("id"),
        listOf("id"),
        listOf("id"),
        listOf("id"),
        "id", "id", "id", "id", "ID"
    ),
    // 意大利语
    IT(
        Res.string.language_it,
        listOf("it", "it-rIT"),
        listOf("it"),
        listOf("it", "it_IT"),
        listOf("it", "it_IT"),
        listOf("it", "it_IT", "it-IT"),
        "it", "it", "it", "it", "IT"
    ),
    // 日语
    JP(
        Res.string.language_jp,
        listOf("ja", "ja-rJP"),
        listOf("ja"),
        listOf("ja", "ja_JP"),
        listOf("ja", "ja_JP"),
        listOf("ja", "ja_JP", "ja-JP"),
        "ja", "jp", "ja", "ja", "JA"
    ),
    // 韩语
    KO(
        Res.string.language_ko,
        listOf("ko", "ko-rKR"),
        listOf("ko"),
        listOf("ko", "ko_KR"),
        listOf("ko", "ko_KR"),
        listOf("ko", "ko_KR", "ko-KR"),
        "ko", "kor", "ko", "ko", "KO"
    ),
    // 荷兰语
    NL(
        Res.string.language_nl,
        listOf("nl", "nl-rNL", "nl-rBE"),
        listOf("nl"),
        listOf("nl", "nl_NL", "nl_BE"),
        listOf("nl", "nl_NL", "nl_BE"),
        listOf("nl", "nl_NL", "nl-NL", "nl_BE", "nl-BE"),
        "nl", "nl", "nl", "", "NL"
    ),
    // 挪威语
    NO(
        Res.string.language_no,
        listOf("nb", "nb-rNO"),
        listOf("nb"),
        listOf("nb", "nb_NO"),
        listOf("nb", "nb_NO"),
        listOf("nb", "nb_NO", "nb-NO"),
        "no", "nor", "nb", "", "NB"
    ),
    // 波兰语
    PL(
        Res.string.language_pl,
        listOf("pl", "pl-rPL"),
        listOf("pl"),
        listOf("pl", "pl_PL"),
        listOf("pl", "pl_PL"),
        listOf("pl", "pl_PL", "pl-PL"),
        "pl", "pl", "pl", "", "PL"
    ),
    // 葡萄牙语
    PT(
        Res.string.language_pt,
        listOf("pt", "pt-rPT"),
        listOf("pt-PT"),
        listOf("pt", "pt_PT"),
        listOf("pt", "pt_PT"),
        listOf("pt", "pt_PT", "pt-PT"),
        "pt", "pt", "pt", "pt", "PT-PT"
    ),
    PT_BR(
        Res.string.language_pt_br,
        listOf("pt-rBR"),
        listOf("pt-BR"),
        listOf("pt_BR"),
        listOf("pt_BR"),
        listOf("pt_BR", "pt-BR"),
        "pt-BR", "pot", "pt-BR", "pt", "PT-BR"
    ),
    // 罗马尼亚语
    RO(
        Res.string.language_ro,
        listOf("ro", "ro-rRO"),
        listOf("ro"),
        listOf("ro", "ro_RO"),
        listOf("ro", "ro_RO"),
        listOf("ro", "ro_RO", "ro-RO"),
        "ro", "rom", "ro", "", "RO"
    ),
    // 俄语
    RU(
        Res.string.language_ru,
        listOf("ru", "ru-rRU"),
        listOf("ru"),
        listOf("ru", "ru_RU"),
        listOf("ru", "ru_RU"),
        listOf("ru", "ru_RU", "ru-RU"),
        "ru", "ru", "ru", "ru", "RU"
    ),
    // 斯洛文尼亚语
    SL(
        Res.string.language_sl,
        listOf("sl", "sl-rSI"),
        listOf("sl"),
        listOf("sl", "sl_SI"),
        listOf("sl", "sl_SI"),
        listOf("sl", "sl_SI", "sl-SI"),
        "sl", "slo", "sl", "", "SL"
    ),
    // 瑞典语
    SV(
        Res.string.language_sv,
        listOf("sv", "sv-rSE"),
        listOf("sv"),
        listOf("sv", "sv_SE"),
        listOf("sv", "sv_SE"),
        listOf("sv", "sv_SE", "sv-SE"),
        "sv", "swe", "sv", "", "SV"
    ),
    // 土耳其语
    TR(
        Res.string.language_tr,
        listOf("tr", "tr-rTR"),
        listOf("tr"),
        listOf("tr", "tr_TR"),
        listOf("tr", "tr_TR"),
        listOf("tr", "tr_TR", "tr-TR"),
        "tr", "tr", "tr", "tr", "TR"
    ),
    // 泰语
    TH(
        Res.string.language_th,
        listOf("th", "th-rTH"),
        listOf("th"),
        listOf("th", "th_TH"),
        listOf("th", "th_TH"),
        listOf("th", "th_TH", "th-TH"),
        "th", "th", "th", "th", "TH"
    ),
    // 乌克兰语
    UK(
        Res.string.language_uk,
        listOf("uk"),
        listOf("uk"),
        listOf("uk"),
        listOf("uk"),
        listOf("uk"),
        "uk", "ukr", "uk", "", "UK"
    ),
    // 越南语
    VI(
        Res.string.language_vi,
        listOf("vi", "vi-rVN"),
        listOf("vi"),
        listOf("vi", "vi_VN"),
        listOf("vi", "vi_VN"),
        listOf("vi", "vi_VN", "vi-VN"),
        "vi", "vie", "vi", "vi", "VI"
    ),
    // 粤语
    YUE(
        Res.string.language_yue,
        listOf("zh-rHK", "zh-rMO"),
        listOf("zh-HK"),
        listOf("zh_HK", "zh_MO"),
        listOf("zh_HK", "zh_Hant_HK", "zh_MO",  "zh_Hant_MO"),
        listOf("zh_HK", "zh-HK", "zh_Hant_HK", "zh-Hant-HK", "zh_MO", "zh-MO", "zh_Hant_MO", "zh-Hant-MO"),
        "yue", "yue", "zh-Hant", "zh-TW", "ZH-HANT"
    ),
    // 中文
    ZH(
        Res.string.language_zh,
        listOf("zh", "zh-rCN", "zh-rSG"),
        listOf("zh-Hans"),
        listOf("zh", "zh_CN", "zh_SG"),
        listOf("zh", "zh_Hans", "zh_Hans_CN", "zh_Hans_SG"),
        listOf("zh", "zh_Hans", "zh-Hans", "zh_Hans_CN", "zh-Hans-CN", "zh_Hans_SG", "zh-Hans-SG"),
        "zh", "zh", "zh-Hans", "zh", "ZH-HANS"
    ),
    // 繁体中文
    CHT(
        Res.string.language_cht,
        listOf("zh-rTW"),
        listOf("zh-Hant"),
        listOf("zh_TW"),
        listOf("zh_Hant", "zh_Hant_TW"),
        listOf("zh_Hant", "zh-Hant", "zh_Hant_TW", "zh-Hant-TW", "zh-TW"),
        "zh-TW", "cht", "zh-Hant", "zh-TW", "ZH-HANT"
    ),
    ;

    /** 判断是否包含指定的语言 */
    fun contains(language: String): Boolean = this.android.contains(language) || this.ios.contains(language)

    @Composable
    fun getReadableName(): String = stringResource(this.nameRes)

}