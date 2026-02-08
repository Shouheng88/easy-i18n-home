package me.shouheng.i18n.i18n.flutter

import io.github.vinceglb.filekit.PlatformFile
import me.shouheng.i18n.i18n.AbsTextResource

/** Flutter .arb 文件词条 */
class FlutterArbResource(
    val name: String,
    val value: String,
    val desc: String?,
    file: PlatformFile,
): AbsTextResource(file) {

    override fun getTextName(): String = name

    override fun getDisplayValue(): String = value

    override fun toString(): String {
        return "FlutterArbResource(name='$name', value='$value', desc='$desc')"
    }
}