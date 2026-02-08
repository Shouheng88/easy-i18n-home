package me.shouheng.i18n.net.server.model.base

import me.shouheng.i18n.data.common.Resource
import java.io.Serializable
import java.util.*

/** 支持任意数据泛型的业务相应 */
open class BaseBusinessResponse<T, B>: Serializable {

    /** Is the response succeed.  */
    var success = false

    /** Response data.  */
    var data: T? = null

    /** Response code.  */
    var code: String? = null

    /** Message for response.  */
    var message: String? = null

    /** Timestamp of server.  */
    var timestamp: Date? = null

    /** Reserved filed.  */
    var udf1: Long? = null

    /** Reserved filed.  */
    var udf2: Double? = null

    /** Reserved filed.  */
    var udf3: Boolean? = null

    /** Reserved filed.  */
    var udf4: String? = null

    /** Reserved filed.  */
    var udf5: B? = null

    /** 将 [BusinessResponse] 转换为 [Resources] */
    fun asResource(): Resource<T> = if (this.success) {
        Resource.success(
            this.data,
            this.udf1,
            this.udf2,
            this.udf3,
            this.udf4,
            this.udf5
        )
    } else {
        Resource.failure(
            this.code,
            this.message,
            this.udf1,
            this.udf2,
            this.udf3,
            this.udf4,
            this.udf5
        )
    }
}

/** 业务响应 */
class BusinessResponse<T> : BaseBusinessResponse<T, Any>()
