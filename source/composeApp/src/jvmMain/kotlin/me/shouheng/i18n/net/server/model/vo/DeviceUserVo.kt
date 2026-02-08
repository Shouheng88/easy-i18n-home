package me.shouheng.i18n.net.server.model.vo

import me.shouheng.i18n.net.server.model.base.AbstractVo
import me.shouheng.i18n.net.server.model.base.Platform
import me.shouheng.i18n.net.server.model.enums.AppGoodsType
import me.shouheng.i18n.net.server.model.enums.DeviceType
import me.shouheng.i18n.utils.DeviceUtils

/**
 * DeviceUserVo
 *
 * @author [Shouheng.W](mailto:shouheng2015@gmail.com)
 * @version 1.0
 * @date 2020/11/09 10:15
 */
class DeviceUserVo : AbstractVo() {
    var appId: Long? = null
    var appName: String? = null
    var deviceType: DeviceType? = null
    var deviceId: String? = null
    var platform: String? = null
    var role: Int? = null

    /** Count of unread messages  */
    var messages: Long? = null
    var userMessages: Long? = null
    var vipInfo: VipInfoVo? = null

    /** The sign is details about the vip and user info, which is used to analyse if
     * the data stored in App is changed by user illegally.  */
    var sign: String? = null
    var token: String? = null

    var vipInfoList: List<VipInfoVo>? = null

    var isVip: Boolean
        get() = role != null && role!! and 0x00000001 > 0
        set(vip) {
            if (role == null) role = 0
            role = if (vip) role!! or 0x00000001 else role!! and -0x2
        }

    var isSVip: Boolean
        get() = role != null && role!! and 0x00000002 > 0
        set(svip) {
            if (role == null) role = 0
            role = if (svip) role!! or 0x00000002 else role!! and -0x3
        }

    /** Client info object get from [platform] */
    var platformObject: Platform? = null

    init {
        this.deviceType = DeviceUtils.getDeviceType()
    }

    /** Judge if user vip */
    fun isUserVip(): Boolean = vipInfoList?.any { it.isValid && it.type?.isVip() == true } == true

    /** Judge if user svip */
    fun isUserSVip(): Boolean = vipInfoList?.any { it.isValid && it.type?.isSVip() == true } == true

    /** Is aux */
    fun isAux(): Boolean = vipInfo != null && vipInfo!!.isValid && vipInfo!!.type!!.isAux()

    /** Is device can be aux. */
    fun canBeAux() = vipInfo == null
            || !vipInfo!!.isValid
            || vipInfo!!.type == AppGoodsType.NORMAL
            || vipInfo!!.type?.isFreeTrail() == true

    /** 是否已经使用了试用 */
    fun isFreeTrialUsed(): Boolean = vipInfoList?.any { it.type?.isFreeTrail() == true } == true
}