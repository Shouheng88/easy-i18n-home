package me.shouheng.i18n.net.server.model.vo

import me.shouheng.i18n.net.server.model.base.AbstractVo

/**
 * AppReviewVo
 *
 * @author [Shouheng.W](mailto:shouheng2015@gmail.com)
 * @version 1.0
 * @date 2022/02/07 06:17
 */
class AppReviewVo : AbstractVo() {
    var appId: Long? = null
    var content: String? = null
    var language: String? = null
    var userName: String? = null
    var userAvatar: String? = null
}

/** App review cache object. */
class AppReviewCache {
    var reviews: List<AppReviewVo>? = null
}
