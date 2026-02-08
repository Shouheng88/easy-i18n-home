package me.shouheng.i18n.data.model

import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.absolutePath
import io.github.vinceglb.filekit.name
import me.shouheng.i18n.I18nPath

/** 扫描到的路径 */
data class PathDetected(
    val file: PlatformFile,
    val root: PlatformFile,
    val type: I18nResourceType,
    val path: I18nPath? = null,
) {

    fun getSimplifiedPath(): String {
        val rootPath = root.absolutePath()
        val filePath = file.absolutePath()
        val relative = filePath.removePrefix(rootPath)
        return "${root.name}${relative}(${type.simpleName()})"
    }

    companion object {
        fun from(path: I18nPath): PathDetected = PathDetected(
            PlatformFile(path.path),
            PlatformFile(""),
            I18nResourceType.from(path.resourceType.toInt()),
            path
        )
    }
}