package me.shouheng.i18n.net.server.model.vo

import me.shouheng.i18n.net.server.model.base.AbstractVo

class AppTopicVo : AbstractVo() {

    var topicGroup: String? = null

    var topicName: String? = null

    var topicCover: String? = null

    var topicBrief: String? = null

    var topicLink: String? = null

    var topicOrder: Int? = null
}