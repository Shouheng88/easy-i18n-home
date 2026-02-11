package me.shouheng.i18n.data.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import easy_i18n.composeapp.generated.resources.*
import io.github.vinceglb.filekit.PlatformFile
import me.shouheng.i18n.I18nPath
import me.shouheng.i18n.I18nWord
import me.shouheng.i18n.data.Colors
import me.shouheng.i18n.i18n.AbsTextResource
import me.shouheng.i18n.i18n.IWordDeleter
import me.shouheng.i18n.i18n.IWordUpdater
import me.shouheng.i18n.i18n.android.AndroidWordDeleter
import me.shouheng.i18n.i18n.android.AndroidWordUpdater
import me.shouheng.i18n.i18n.flutter.FlutterArbDeleter
import me.shouheng.i18n.i18n.flutter.FlutterArbUpdater
import me.shouheng.i18n.i18n.ios.IOSDotStringWordDeleter
import me.shouheng.i18n.i18n.ios.IOSDotStringWordUpdater
import me.shouheng.i18n.i18n.ios.IOSXCStringWordDeleter
import me.shouheng.i18n.i18n.ios.IOSXCStringWordUpdater
import me.shouheng.i18n.i18n.java.PropertiesWordDeleter
import me.shouheng.i18n.i18n.java.PropertiesWordUpdater
import me.shouheng.i18n.i18n.json.JsonWordDeleter
import me.shouheng.i18n.i18n.json.JsonWordUpdater
import me.shouheng.i18n.i18n.yaml.YamlWordDeleter
import me.shouheng.i18n.i18n.yaml.YamlWordUpdater
import me.shouheng.i18n.utils.ITextGetter
import me.shouheng.i18n.utils.LanguageNameGetter
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.stringResource

/** 一个词条中存在多个文件类型的 */
class SingleFileI18WordModel(
    /** 词条的名称 */
    name: String,
    /** 不同语言中的含义。*/
    meanings: List<Meaning>,
    /** 描述性文案：用于给 AI 提示*/
    description: String? = null,
    /** 数据库中的对象 */
    dto: I18nWord? = null,
    /** 所在的路径 */
    path: I18nPath,
    /** 在文件中的位置 */
    order: Int,
    /** 词条的文件 */
    val origin: AbsTextResource,
    /** 删除器 */
    deleter: IWordDeleter,
    /** 更新器 */
    updater: IWordUpdater,
): I18nWordModel(name, meanings, description, dto, path, order, deleter, updater) {

    override fun getSourceLanguage(): String? = origin.getSourceLanguage()

}

/** 一个词条 */
open class I18nWordModel internal constructor(
    /** 词条的名称 */
    val name: String,
    /** 不同语言中的含义。*/
    val meanings: List<Meaning>,
    /** 描述性文案：用于给 AI 提示*/
    val description: String? = null,
    /** 数据库中的对象 */
    val dto: I18nWord? = null,
    /** 所在的路径 */
    var path: I18nPath,
    /** 在文件中的位置 */
    val order: Int,
    /** 删除器 */
    val deleter: IWordDeleter,
    /** 更新器 */
    val updater: IWordUpdater,
) {

    /** 是否可能为复数类型 */
    val isPossiblePlural: Boolean = meanings.any { it.isPlural }

    /** 是否可能为数组类型 */
    val isPossibleArray: Boolean = meanings.any { it.isArray }

    /** 翻译比 */
    val percentage: Int

    init {
        val anyUnTranslatable = meanings.any { it.origin != null && !it.origin.isTranslatable() }
        percentage = if (anyUnTranslatable) 100 else
            meanings.count { it.origin != null } * 100 / meanings.count()
    }

    /** 该词是否需要翻译 */
    fun isNeedTranslate(): Boolean {
        val anyUnTranslatable = meanings.any { it.origin != null && !it.origin.isTranslatable() }
        return !anyUnTranslatable && meanings.any { it.isNeedTranslate() }
    }

    /** 计算需要翻译的词条数量 */
    fun countNeedTranslate(): Int {
        val anyUnTranslatable = meanings.any { it.origin != null && !it.origin.isTranslatable() }
        if (anyUnTranslatable) return 0
        return meanings.count { it.isNeedTranslate() }
    }

    /** 源 语言 */
    open fun getSourceLanguage(): String? = null

    /** 翻译比的颜色 */
    fun getPercentageColor(): Color {
        return if (percentage >= 100) {
            Colors.percentage100
        } else if (percentage >= 60) {
            Colors.percentage60
        } else {
            Colors.percentage30
        }
    }

    /** 翻译比的图标 */
    fun getPercentageIcon(): DrawableResource {
        if (percentage >= 100) {
            return Res.drawable.ic_percentage_100
        } else if (percentage >= 90) {
            return Res.drawable.ic_percentage_90
        } else if (percentage >= 80) {
            return Res.drawable.ic_percentage_80
        } else if (percentage >= 70) {
            return Res.drawable.ic_percentage_70
        } else if (percentage >= 60) {
            return Res.drawable.ic_percentage_60
        } else if (percentage >= 50) {
            return Res.drawable.ic_percentage_50
        } else if (percentage >= 40) {
            return Res.drawable.ic_percentage_40
        } else if (percentage >= 30) {
            return Res.drawable.ic_percentage_30
        } else if (percentage >= 20) {
            return Res.drawable.ic_percentage_20
        } else if (percentage >= 10) {
            return Res.drawable.ic_percentage_10
        } else {
            return Res.drawable.ic_percentage_0
        }
    }

    /** 类型信息展示 */
    @Composable
    fun getTypeDisplayName(): String {
        return if (isPossibleArray)
            "(${stringResource(Res.string.word_array_name)})"
        else if (isPossiblePlural)
            "(${stringResource(Res.string.word_plural_name)})"
        else ""
    }

    companion object {

        fun ofArb(
            name: String,
            meanings: List<Meaning>,
            description: String? = null,
            dto: I18nWord? = null,
            path: I18nPath,
            order: Int,
        ): I18nWordModel = I18nWordModel(
            name, meanings, description, dto, path, order, FlutterArbDeleter, FlutterArbUpdater
        )

        fun ofProperties(
            name: String,
            meanings: List<Meaning>,
            description: String? = null,
            dto: I18nWord? = null,
            path: I18nPath,
            order: Int,
        ): I18nWordModel = I18nWordModel(
            name, meanings, description, dto, path, order, PropertiesWordDeleter, PropertiesWordUpdater
        )

        fun ofAndroid(
            name: String,
            meanings: List<Meaning>,
            description: String? = null,
            dto: I18nWord? = null,
            path: I18nPath,
            order: Int,
        ): I18nWordModel = I18nWordModel(
            name, meanings, description, dto, path, order, AndroidWordDeleter, AndroidWordUpdater
        )

        fun ofDotString(
            name: String,
            meanings: List<Meaning>,
            description: String? = null,
            dto: I18nWord? = null,
            path: I18nPath,
            order: Int,
        ): I18nWordModel = I18nWordModel(
            name, meanings, description, dto, path, order, IOSDotStringWordDeleter, IOSDotStringWordUpdater
        )

        fun ofXCString(
            name: String,
            meanings: List<Meaning>,
            description: String? = null,
            dto: I18nWord? = null,
            path: I18nPath,
            order: Int,
            origin: AbsTextResource,
        ): I18nWordModel = SingleFileI18WordModel(
            name, meanings, description, dto, path, order, origin, IOSXCStringWordDeleter, IOSXCStringWordUpdater
        )

        fun ofJson(
            name: String,
            meanings: List<Meaning>,
            description: String? = null,
            dto: I18nWord? = null,
            path: I18nPath,
            order: Int,
        ): I18nWordModel = I18nWordModel(
            name, meanings, description, dto, path, order, JsonWordDeleter(), JsonWordUpdater()
        )

        fun ofYaml(
            name: String,
            meanings: List<Meaning>,
            description: String? = null,
            dto: I18nWord? = null,
            path: I18nPath,
            order: Int,
        ): I18nWordModel = I18nWordModel(
            name, meanings, description, dto, path, order, YamlWordDeleter(), YamlWordUpdater()
        )
    }

    /** 含义 */
    class Meaning private constructor(
        /** 语言：实际上就是各个文件。*/
        val language: String,
        /** 文件：文件对象还是需要的，因为 origin 可能为 null */
        val file: PlatformFile,
        /** 原始多语言对象 */
        val origin: AbsTextResource?,
    ) {

        val isPlural: Boolean = origin?.isPlural() == true

        val isArray: Boolean = origin?.isArray() == true

        /** 获取语言展示的文案 */
        val languageNameGetter: ITextGetter = LanguageNameGetter(language)

        /** 该词是否需要翻译 */
        fun isNeedTranslate(): Boolean = origin == null || origin.getDisplayValue().isEmpty()

        companion object Companion {
            fun from(
                language: String,
                file: PlatformFile,
                resource: AbsTextResource?,
            ): Meaning {
                return Meaning(
                    language,
                    file,
                    resource,
                )
            }
        }
    }
}