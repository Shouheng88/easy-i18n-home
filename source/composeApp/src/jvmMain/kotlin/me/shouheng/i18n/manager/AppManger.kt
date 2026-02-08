package me.shouheng.i18n.manager

import com.russhwolf.settings.contains
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.jsonPrimitive
import me.shouheng.i18n.data.Const.APP_CONFIG_ITEM_VERSION_INFO
import me.shouheng.i18n.data.Const.APP_CONFIG_ITEM_VERSION_INFO_FORCE_UPGRADE
import me.shouheng.i18n.data.Const.APP_CONFIG_ITEM_VERSION_INFO_LATEST_VERSION
import me.shouheng.i18n.data.Event
import me.shouheng.i18n.data.common.AppLanguage
import me.shouheng.i18n.data.theme.AppTheme
import me.shouheng.i18n.utils.AppUtils
import me.shouheng.i18n.utils.extension.md5
import me.shouheng.i18n.utils.settings
import java.util.*
import kotlin.math.min

/** 应用管理 */
object AppManger {

    private const val KEY_THEME = "__app_theme__"

    private const val KEY_LANGUAGE = "__app_language__"

    private val KEY_INSTALL_TIME = "__install_time__".md5()

    private var upgradeDialogDisplayed: Boolean = false

    /** 设置主题 */
    fun setTheme(theme: AppTheme) {
        settings.putInt(KEY_THEME, theme.id)
        post(Event.EVENT_THEME_CHANGED)
    }

    /** 获取当前主题 */
    fun getTheme(): AppTheme = AppTheme.from(settings.getInt(KEY_THEME, AppTheme.SYSTEM.id))

    /** 设置应用多语言  */
    fun setAppLanguage(language: AppLanguage) {
        settings.putInt(KEY_LANGUAGE, language.id)
    }

    /** 获取应用多语言  */
    fun getAppLanguage(): AppLanguage = AppLanguage.from(
        settings.getInt(KEY_LANGUAGE, AppLanguage.SYSTEM.id)
    )

    /** 初始化应用的多语言 */
    fun initAppLanguage() {
        val language = getAppLanguage()
        if (language != AppLanguage.SYSTEM) {
            Locale.setDefault(language.locale)
        }
    }

    /** 是否应该显示升级对话框 */
    fun shouldShowUpgradeDialog(forceToShow: Boolean = false): Boolean = false

    /** 是否强制更新 */
    fun isForceUpgrade(): Boolean = false

    /** 指定的版本是否被忽略 */
    fun isVersionIgnored(version: String): Boolean {
        val keyIgnoreVersions = "__ignored_upgrade_versions__"
        val ignored = settings.getString(keyIgnoreVersions, "")
        return ignored.contains(version)
    }

    /** 忽视指定的版本 */
    fun changeIgnoreState(version: String, ignore: Boolean) {
        val keyIgnoreVersions = "__ignored_upgrade_versions__"
        val separator = ","
        val ignored = settings.getString(keyIgnoreVersions, "").split(separator).toMutableList()
        if (ignore && !ignored.contains(version)) {
            ignored.add(version)
        } else if (!ignore && ignored.contains(version)) {
            ignored.remove(version)
        }
        val newText = ignored.joinToString(separator)
        settings.putString(keyIgnoreVersions, newText)
    }

    /** Is newer version found */
    fun isNewerVersionFound(): Boolean = false

    /** Get newer version */
    @JvmStatic
    fun getNewVersion(): String = ""

    /** 记住安装时间 */
    fun rememberInstallTime() {
        if (!settings.contains(KEY_INSTALL_TIME)) {
            settings.putLong(KEY_INSTALL_TIME, Date().time)
        }
    }

    /** 获取安装时间 */
    fun getInstallTime(): Long = settings.getLong(KEY_INSTALL_TIME, 0L)

    private fun compareVersion(v1: String?, v2: String?): Int {
        if (v1 == null || v2 == null || v1 == v2) {
            return 0
        }
        // 分割字符串
        val arrV1 = v1.split("[._]".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val arrV2 = v2.split("[._]".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        // 从第一位开始进行比较
        var index = 0
        val minLen = min(arrV1.size, arrV2.size)
        var diff: Long = arrV1[index].toLong() - arrV2[index].toLong()
        while (index < minLen && diff == 0L) {
            diff = arrV1[index].toLong() - arrV2[index].toLong()
            index++
        }
        // 将最终的结果再次进行进行比较
        if (diff == 0L) {
            for (i in index until arrV1.size) {
                if (arrV1[i].toLong() > 0) {
                    return 1
                }
            }
            for (i in index until arrV2.size) {
                if (arrV2[i].toLong() > 0) {
                    return -1
                }
            }
            return 0
        } else {
            return if (diff > 0) 1 else -1
        }
    }
}