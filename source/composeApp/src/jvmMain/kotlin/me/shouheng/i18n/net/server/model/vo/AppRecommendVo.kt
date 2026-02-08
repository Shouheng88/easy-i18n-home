package me.shouheng.i18n.net.server.model.vo

import me.shouheng.i18n.net.server.model.base.AbstractVo

/**
 * AppRecommendVo
 *
 * @author [Shouheng.W](mailto:shouheng2015@gmail.com)
 * @version 1.0
 * @date 2022/02/07 07:06
 */
class AppRecommendVo : AbstractVo() {
    var appId: Long? = null
    var name: String? = null
    var icon: String? = null
    var descriptions: String? = null
    var link: String? = null
}