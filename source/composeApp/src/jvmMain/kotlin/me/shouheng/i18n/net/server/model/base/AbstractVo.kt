package me.shouheng.i18n.net.server.model.base

import java.io.Serializable
import java.util.Date

/**
 * 对象的封装
 *
 * @author <a href="mailto:shouheng2015@gmail.com">WngShhng</a>
 * @version 2019-10-03 09:19
 */
abstract class AbstractVo : Serializable {

    var localId: Long? = null

    var id: Long? = null

    var remark: String = ""

    var createdTime: Date = Date(0)

    var updatedTime: Date = Date(0)

    override fun toString(): String {
        return "AbstractVo(localId=$localId, id=$id, " +
                "remark='$remark', " +
                "createdTime=$createdTime, " +
                "updatedTime=$updatedTime)"
    }
}