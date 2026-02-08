package me.shouheng.i18n

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import me.shouheng.i18n.manager.AppManger
import me.shouheng.i18n.manager.DebugManager
import me.shouheng.i18n.net.server.NetworkApi

/** 初始化类 */
object PanGu {

    /** 执行初始化逻辑 */
    fun init() {
        DebugManager.registerCrashHandler()
        AppManger.initAppLanguage()
        NetworkApi.setHosts(listOf("https://meiyan.tech/", "http://dev.meiyan.tech/"))
        AppManger.rememberInstallTime()
        Napier.base(DebugAntilog())
    }
}