package me.shouheng.i18n.net.server.model.so

import me.shouheng.i18n.net.server.model.base.SearchObject

/**
 * RedeemCodeSo
 *
 * @author [Shouheng.W](mailto:shouheng2015@gmail.com)
 * @version 1.0
 * @date 2022/02/07 06:17
 */
class RedeemCodeSo : SearchObject() {
    var code: String? = null
    var appId: Long? = null
    var appGoodsId: Long? = null
}