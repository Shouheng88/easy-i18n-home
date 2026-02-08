package me.shouheng.i18n.net.server.model.vo

import me.shouheng.i18n.net.server.model.base.AbstractVo

/**
 * ForbiddenActionVo
 *
 * @author [Shouheng.W](mailto:shouheng2015@gmail.com)
 * @version 1.0
 * @date 2022/02/20 12:49
 */
class ForbiddenActionVo : AbstractVo() {
    var appId: Long? = null
    var version: String? = null
    var deviceId: Long? = null
    var userId: Long? = null
    var userToken: String? = null
    var action: Long? = null
}