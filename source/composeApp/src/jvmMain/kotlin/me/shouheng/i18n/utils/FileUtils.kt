package me.shouheng.i18n.utils

import me.shouheng.i18n.I18nPath
import me.shouheng.i18n.utils.extension.loge
import java.awt.Desktop
import java.io.File

/** 文件 帮助类 */
object FileUtils {

    /** 打开指定的目录 */
    fun openDirectory(path: I18nPath?) {
        // 唤起系统默认文件管理器并打开目录
        val path = path?.path ?: return
        val directory = File(path)
        openDirectory(directory)
    }

    /** 打开指定的目录 */
    fun openDirectory(directory: File) {
        if (!directory.exists() || !directory.isDirectory) {
            return
        }
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().open(directory)
        } else {
            //  fallback：针对极少数不支持 Desktop 的环境（如 headless 模式）
            executeSystemCommand(directory)
        }
    }

    /** 系统命令 fallback：当 Desktop 不支持时，通过系统命令调用文件管理器 */
    private fun executeSystemCommand(directory: File) {
        val prop = System.getProperty("os.name").lowercase()
        val command = when {
            prop.contains("windows") -> "explorer.exe /select,\"${directory.absolutePath}\""
            prop.contains("mac") -> "open \"${directory.absolutePath}\""
            else -> "xdg-open \"${directory.absolutePath}\"" // Linux
        }
        try {
            Runtime.getRuntime().exec(command)
        } catch (e: Exception) {
            e.printStackTrace()
            loge { "系统命令调用失败：${e.message}" }
        }
    }
}