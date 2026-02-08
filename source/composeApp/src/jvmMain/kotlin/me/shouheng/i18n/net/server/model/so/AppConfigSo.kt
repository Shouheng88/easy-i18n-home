package me.shouheng.i18n.net.server.model.so

import me.shouheng.i18n.net.server.model.base.SearchObject
import me.shouheng.i18n.net.server.model.enums.DeviceType
import me.shouheng.i18n.utils.AppUtils
import me.shouheng.i18n.utils.DeviceUtils

/**
 * AppConfigSo
 *
 * @author [Shouheng.W](mailto:shouheng2015@gmail.com)
 * @version 1.0
 * @date 2020/11/09 10:15
 */
class AppConfigSo private constructor(): SearchObject() {
    var appId: Long? = null
    var platform: DeviceType? = null
    var currentVersion: String? = null
    var channel: String? = null

    companion object {
        fun of(): AppConfigSo {
            val so = AppConfigSo()
            so.platform = DeviceUtils.getDeviceType()
            so.currentVersion = AppUtils.getAppVersionName()
            return so
        }
    }
}