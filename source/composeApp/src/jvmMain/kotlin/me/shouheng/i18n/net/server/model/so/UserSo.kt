package me.shouheng.i18n.net.server.model.so

import me.shouheng.i18n.net.server.model.base.SearchObject
import me.shouheng.i18n.net.server.model.enums.AppGoodsType
import me.shouheng.i18n.net.server.model.enums.CodeType

/**
 * UserSo
 *
 * @author [Shouheng.W](mailto:shouheng2015@gmail.com)
 * @version 1.0
 * @date 2021/01/11 09:57
 */
class UserSo : SearchObject() {
    var appId: Long? = null
    var userId: Long? = null
    var account: String? = null
    var code: String? = null
    var codeType: CodeType? = null
    var password: String? = null
    var userIds: List<Long>? = null
    var devId: Long? = null

    /** Used for exchange rights.
     * [.fromDevId] is the device to exchange right from,
     * [.toDevId] is the device to exchange right to.  */
    var fromDevId: Long? = null
    var toDevId: Long? = null

    var goodsType: AppGoodsType? = null
}