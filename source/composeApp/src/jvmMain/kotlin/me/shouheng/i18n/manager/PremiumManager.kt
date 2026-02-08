package me.shouheng.i18n.manager

import me.shouheng.i18n.net.server.model.enums.PayWay
import me.shouheng.i18n.utils.settings

/** 付费和用户相关的管理类 */
object PremiumManager {

    private const val KEY_PAY_WAY = "__pay_way__"

    /** 上次选择的付款方式 */
    fun getLastPayWay(): PayWay? {
        val id = settings.getIntOrNull(KEY_PAY_WAY) ?: return null
        return PayWay.getTypeById(id)
    }

    fun rememberPayWay(payWay: PayWay) {
        settings.putInt(KEY_PAY_WAY, payWay.id)
    }
}