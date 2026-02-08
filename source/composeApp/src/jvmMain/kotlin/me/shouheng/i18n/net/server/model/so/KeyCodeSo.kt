package me.shouheng.i18n.net.server.model.so

import me.shouheng.i18n.data.Const
import me.shouheng.i18n.net.server.model.base.SearchObject

class KeyCodeSo : SearchObject() {

    var name: String? = null

    var app: String? = Const.APP_CODE
}