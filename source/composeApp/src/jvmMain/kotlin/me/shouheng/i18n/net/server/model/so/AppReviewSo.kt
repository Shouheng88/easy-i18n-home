package me.shouheng.i18n.net.server.model.so

import me.shouheng.i18n.net.server.model.base.SearchObject

/**
 * AppReviewSo
 *
 * @author [Shouheng.W](mailto:shouheng2015@gmail.com)
 * @version 1.0
 * @date 2022/02/07 06:17
 */
class AppReviewSo : SearchObject() {
    var appId: Long? = null

    /** 要搜索的评论的语言  */
    var languages: List<String>? = null

    var language: String? = null

    var app: String? = null
}