package me.shouheng.i18n.net.server.model.base

import me.shouheng.i18n.data.Const
import me.shouheng.i18n.utils.extension.sha1
import java.io.Serializable
import java.util.*

/** 请求信息封装类 */
class BusinessRequest<T> : Serializable {

    /** Request id used to specify given request.  */
    var requestId: String? = null

    /** User id  */
    var userId: Long? = null

    /** User token.  */
    var token: String? = null

    /** Encrypt request key.  */
    var key: String? = null

    /** The App id  */
    val appId: Long = Const.APP_ID

    /** The client information.  */
    var clientInfo: ClientInfo? = null

    /** App request parameters.  */
    var apiParams: T? = null

    companion object {

        /**
         * 获取一个请求实例
         *
         * @param apiParams 请求参数
         * @param userDomain 请求的领域信息，true 表示是用户请求，false 表示设备级的请求
         */
        suspend fun <P> of(apiParams: P, userDomain: Boolean = false) = BusinessRequest<P>().apply {
            this.requestId = generateRequestId(this)
            this.apiParams = apiParams
            this.clientInfo = ClientInfo.newInstance()
        }

        /** 为当前请求生成一个独立的 ID */
        private fun generateRequestId(request: BusinessRequest<*>): String = runCatching {
            (
                "${request.clientInfo?.id?:""}+" +
                    "${request.clientInfo?.time?.time}+" +
                    "${Random().nextInt()}"
            ).sha1().lowercase()
        }.getOrNull() ?: "error-${System.currentTimeMillis()}"
    }
}
