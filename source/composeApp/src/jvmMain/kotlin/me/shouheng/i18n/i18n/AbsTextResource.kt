package me.shouheng.i18n.i18n

import io.github.vinceglb.filekit.PlatformFile

/** 应用的词条资源 数据类 */
abstract class AbsTextResource(val file: PlatformFile) {

    /** 获取名称 */
    abstract fun getTextName(): String

    /** 获取用于展示的值 */
    abstract fun getDisplayValue(): String

    /** 当前应用是否支持编辑和展示这类的值 */
    open fun isSupport(): Boolean = true

    /** 这种类型的值是否可以翻译 */
    open fun isTranslatable(): Boolean = true

    /** 是否为复数类型 */
    open fun isPlural(): Boolean = false

    /** 是否为数组类型 */
    open fun isArray(): Boolean = false

    /** 获取 source language */
    open fun getSourceLanguage(): String? = null
}