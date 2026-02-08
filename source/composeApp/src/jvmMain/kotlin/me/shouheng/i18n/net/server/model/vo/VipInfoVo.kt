package me.shouheng.i18n.net.server.model.vo

import me.shouheng.i18n.net.server.model.base.AbstractVo
import me.shouheng.i18n.net.server.model.enums.AppGoodsType
import java.util.Date

/**
 * VipInfoVo
 *
 * @author [Shouheng.W](mailto:shouheng2015@gmail.com)
 * @version 1.0
 * @date 2020/11/09 05:15
 */
class VipInfoVo : AbstractVo() {
    var userId: Long? = null
    var name: String? = null
    var startTime: Date? = null
    var endTime: Date? = null
    var type: AppGoodsType? = null
    val isValid: Boolean
        get() {
            val current = System.currentTimeMillis()
            return endTime == null || endTime!!.time > current
        }
}