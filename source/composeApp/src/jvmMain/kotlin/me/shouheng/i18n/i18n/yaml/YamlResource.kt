package me.shouheng.i18n.i18n.yaml

import io.github.vinceglb.filekit.PlatformFile
import me.shouheng.i18n.i18n.AbsTextResource

data class YamlResource(
    val key: String,
    val value: String,
    val fileRef: PlatformFile
) : AbsTextResource(fileRef) {

    override fun getTextName(): String = key

    override fun getDisplayValue(): String = value
}
