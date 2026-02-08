package me.shouheng.i18n.net.server.model.so

import me.shouheng.i18n.net.server.model.base.SearchObject
import me.shouheng.i18n.net.server.model.enums.AppPaymentStatus
import me.shouheng.i18n.net.server.model.enums.PayWay

/**
 * AppPaymentSo
 *
 * @author [Shouheng.W](mailto:shouheng2015@gmail.com)
 * @version 1.0
 * @date 2020/11/09 05:15
 */
class AppPaymentSo : SearchObject() {
    var id: Long? = null
    var userId: Long? = null
    var payWay: PayWay? = null
    var serial: String? = null
    var status: AppPaymentStatus? = null
}