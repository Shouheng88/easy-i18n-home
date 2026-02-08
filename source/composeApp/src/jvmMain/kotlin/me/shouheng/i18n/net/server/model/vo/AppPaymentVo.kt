package me.shouheng.i18n.net.server.model.vo

import androidx.compose.runtime.Composable
import me.shouheng.i18n.net.server.model.base.AbstractVo
import me.shouheng.i18n.net.server.model.enums.AppPaymentStatus
import me.shouheng.i18n.net.server.model.enums.PayWay
import org.jetbrains.compose.resources.stringResource

/**
 * AppPaymentVo
 *
 * @author [Shouheng.W](mailto:shouheng2015@gmail.com)
 * @version 1.0
 * @date 2020/11/09 05:15
 */
class AppPaymentVo : AbstractVo() {
    var appId: Long? = null
    var appName: String? = null
    var appGoodsId: Long? = null
    var serial: String? = null
    var deviceUserId: Long? = null
    var status: AppPaymentStatus? = null
    var payWay: PayWay? = null
    var oweMoney: Int? = null
    var oweMoneyType: String? = null
    /** The google goods id  */
    var googleGoodsId: String? = null
    /** The google goods purchase token  */
    var googlePurchaseToken: String? = null
    /** The google goods purchase package name  */
    var googlePackageName: String? = null

    /** 获取状态的文本 */
    @Composable
    fun getStatusText(): String {
        val status = status
        status ?: return ""
        return stringResource(status.nameRes)
    }

    /** 获取付费方式的文本 */
    @Composable
    fun getPayWayText(): String {
        val payWay = payWay ?: return ""
        return stringResource(payWay.nameRes)
    }
}