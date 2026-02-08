package me.shouheng.i18n.net.server.model.vo

import me.shouheng.i18n.net.server.model.base.AbstractVo

/**
 * UserMessageVo
 *
 * @author [Shouheng.W](mailto:shouheng2015@gmail.com)
 * @version 1.0
 * @date 2022/01/09 05:46
 */
class UserMessageVo : AbstractVo() {
    var content: String? = null
    /** 该消息是否被用户已读。  */
    var read: Boolean? = null
    var display: String? = null
}