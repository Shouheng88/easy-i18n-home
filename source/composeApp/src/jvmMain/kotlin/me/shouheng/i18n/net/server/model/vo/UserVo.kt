package me.shouheng.i18n.net.server.model.vo

import me.shouheng.i18n.net.server.model.base.AbstractVo
import java.util.*

/**
 * UserVo
 *
 * @author [Shouheng.W](mailto:shouheng2015@gmail.com)
 * @version 1.0
 * @date 2021/01/11 09:57
 */
class UserVo : AbstractVo() {
    var appId: Long? = null
    var appName: String? = null
    var account: String? = null
    var password: String? = null
    var nickName: String? = null
    var gender: String? = null
    var role: String? = null
    var avatar: String? = null
    var birthday: Date? = null
    var location: String? = null
    var school: String? = null
    var organization: String? = null
    var introduction: String? = null
    var token: String? = null
    var expireIn: Date? = null
    var devices: List<DeviceUserVo>? = null
}