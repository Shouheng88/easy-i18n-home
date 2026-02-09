package me.shouheng.i18n.net.server.model.so

import me.shouheng.i18n.net.server.model.base.SearchObject

/**
 * AppRecommendSo
 *
 * @author [Shouheng.W](mailto:shouheng2015@gmail.com)
 * @version 1.0
 * @date 2022/02/07 07:06
 */
class AppRecommendSo : SearchObject() {
    var app: String? = null
    var channel: String? = null
}