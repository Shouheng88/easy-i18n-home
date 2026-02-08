package me.shouheng.i18n.i18n.java

import io.github.vinceglb.filekit.PlatformFile
import me.shouheng.i18n.i18n.AbsTextResource

/** Java Properties 多语言词条 */
class PropertyResource(
    val name: String,
    val value: String,
    file: PlatformFile,
): AbsTextResource(file) {

    override fun getTextName(): String = name

    override fun getDisplayValue(): String = value
}