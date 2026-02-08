package me.shouheng.i18n.net.server.model.so

import me.shouheng.i18n.data.Const
import me.shouheng.i18n.net.server.model.base.SearchObject

/**
 * DeviceUserSo
 *
 * @author [Shouheng.W](mailto:shouheng2015@gmail.com)
 * @version 1.0
 * @date 2020/11/09 10:15
 */
class DeviceUserSo : SearchObject() {
    var id: Long? = null
    val appId: Long = Const.APP_ID
    /** 正式用户的 id 和 token */
    var userId: Long? = null
    var userToken: String? = null
}