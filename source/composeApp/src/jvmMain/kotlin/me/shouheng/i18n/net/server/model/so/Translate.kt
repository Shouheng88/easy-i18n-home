package me.shouheng.i18n.net.server.model.so

import me.shouheng.i18n.net.server.model.base.SearchObject
import me.shouheng.i18n.net.server.model.enums.TranslateEngine

class TranslateSo : SearchObject() {
    var text: String? = null
    var target: String? = null
    var engine: TranslateEngine? = null
}