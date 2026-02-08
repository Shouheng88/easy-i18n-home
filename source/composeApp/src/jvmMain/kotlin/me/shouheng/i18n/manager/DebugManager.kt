package me.shouheng.i18n.manager

import me.shouheng.i18n.data.Const
import me.shouheng.i18n.utils.DeviceUtils
import me.shouheng.i18n.utils.extension.loge
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter
import java.net.UnknownHostException
import java.text.SimpleDateFormat
import java.util.*

/** 调试工具 */
object DebugManager {

    /** 注册崩溃捕获 */
    fun registerCrashHandler() {
        val handler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { t, e ->
            loge { "捕获到了异常" }
            e.printStackTrace()
            writeCrashLog(e)
            handler?.uncaughtException(t, e)
        }
    }

    /** 获取崩溃日志的数量 */
    fun getCrashLogCount(): Int {
        val crashDir = getCrashesDirectory()
        return crashDir.list().count()
    }

    /** 写入崩溃日志 */
    private fun writeCrashLog(e: Throwable) {
        val crashDir = getCrashesDirectory()
        val date = Date()
        val crashFile = File(crashDir, SimpleDateFormat("yyyyMMddHHmmssSSS").format(date) + ".txt")
        var text = throwable2String(e)
        text = """
OS: ${DeviceUtils.getOSName()} ${DeviceUtils.getOSVersion()} ${DeviceUtils.getOSArch()}
Time: ${SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(date)}
Java: ${DeviceUtils.getJavaVersion()}
Error stack:
$text
        """.trimIndent()
        crashFile.writeText(text)
    }

    /** 获取崩溃日志的目录 */
    fun getCrashesDirectory(): File {
        val userHome = System.getProperty("user.home")
        val crashDir = File(userHome, ".${Const.APP_NAME_ENGLISH}/crashes")
        crashDir.mkdirs()
        return crashDir
    }

    /** 获取日志信息 */
    private fun throwable2String(e: Throwable): String {
        var t: Throwable? = e
        while (t != null) {
            if (t is UnknownHostException) {
                return ""
            }
            t = t.cause
        }
        val sw = StringWriter()
        val pw = PrintWriter(sw)
        e.printStackTrace(pw)
        var cause = e.cause
        while (cause != null) {
            cause.printStackTrace(pw)
            cause = cause.cause
        }
        pw.flush()
        return sw.toString()
    }
}