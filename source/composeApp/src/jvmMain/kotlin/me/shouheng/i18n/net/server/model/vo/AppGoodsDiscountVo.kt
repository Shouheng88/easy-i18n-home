package me.shouheng.i18n.net.server.model.vo

import me.shouheng.i18n.net.server.model.base.AbstractVo
import java.util.*

/**
 * AppGoodsDiscountVo
 *
 * @author [Shouheng.W](mailto:shouheng2015@gmail.com)
 * @version 1.0
 * @date 2020/11/09 10:15
 */
class AppGoodsDiscountVo : AbstractVo() {
    var appGoodsId: Long? = null
    var discount: Int? = null
    var startTime: Date? = null
    var endTime: Date? = null
    var brief: String? = null
    var onSale: Boolean? = null
    val isValid: Boolean
        get() {
            val current = System.currentTimeMillis()
            return current > startTime!!.time && current < endTime!!.time
        }
}