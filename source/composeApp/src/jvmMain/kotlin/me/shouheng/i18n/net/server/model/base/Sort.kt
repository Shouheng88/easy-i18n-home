package me.shouheng.i18n.net.server.model.base

import java.io.Serializable

/** 排序对象 */
class Sort : Serializable {

    var sortKey: String? = null

    var sortDir: String? = null

    constructor(sortKey: String) {
        this.sortKey = sortKey
    }

    constructor(sortKey: String, sortDir: String) {
        this.sortKey = sortKey
        this.sortDir = sortDir
    }

    companion object {

        @JvmStatic
        fun valueOf(sortKey: String): Sort {
            return Sort(sortKey)
        }

        @JvmStatic
        fun valueOf(sortKey: String, sortDir: String): Sort {
            return Sort(sortKey, sortDir)
        }
    }
}
