package me.shouheng.i18n.net.server.model.vo

import me.shouheng.i18n.data.Const
import me.shouheng.i18n.net.server.model.base.AbstractVo
import me.shouheng.i18n.net.server.model.enums.FeedbackType

/**
 * FeedbackVo
 *
 * @author [Shouheng.W](mailto:shouheng2015@gmail.com)
 * @version 1.0
 * @date 2020/11/09 10:15
 */
class FeedbackVo : AbstractVo() {
    val appId: Long = Const.APP_ID
    var type: FeedbackType? = null
    var content: String? = null
    var contact: String? = null
    /** 注册用户 ID */
    var userId: Long? = null
    /** 注册用户账号  */
    var userAccount: String? = null
}