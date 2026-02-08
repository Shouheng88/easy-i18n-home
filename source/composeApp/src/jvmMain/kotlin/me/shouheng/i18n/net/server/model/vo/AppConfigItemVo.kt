package me.shouheng.i18n.net.server.model.vo

import me.shouheng.i18n.net.server.model.base.AbstractVo
import me.shouheng.i18n.net.server.model.enums.ConfigValueType

/**
 * AppConfigItemVo
 *
 * @author [Shouheng.W](mailto:shouheng2015@gmail.com)
 * @version 1.0
 * @date 2024/04/01 09:56
 */
class AppConfigItemVo : AbstractVo() {
    /** 配置的 key */
    var configKey: String? = null
    /** 配置的类型 */
    var type: ConfigValueType? = null
    /** 配置的值  */
    var value: String? = null

    override fun toString(): String {
        return "AppConfigItemVo(configKey=$configKey, type=$type, value=$value)"
    }
}
