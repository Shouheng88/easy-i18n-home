package me.shouheng.i18n.net.server.interceptor

import com.google.gson.JsonObject
import me.shouheng.i18n.data.Event
import me.shouheng.i18n.manager.post
import me.shouheng.i18n.net.server.NetworkApi
import me.shouheng.i18n.utils.JSON
import me.shouheng.i18n.utils.extension.loge
import me.shouheng.i18n.utils.isPossibleDebug
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

/** 服务器逻辑失败拦截器 */
class ServerFailureInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var response = chain.proceed(request)
        val jsonType = "application/json".toMediaType()
        val url = request.url.toString()
        if (response.body.contentType() == jsonType && isOwnHost(url)) {
            if (response.code == NetworkApi.CODE_SUCCESS) {
                val json = response.body.string()
                if (isPossibleDebug()) {
                    println("""
>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> begin
Network request: $url
Response: 
$json
<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< end
                        """.trimIndent()
                    )
                }
                val isUserDomain = NetworkApi.isUserDomain(url)
                try {
                    val jsonObject = JSON.from(json, JsonObject::class.java)
                    if (jsonObject?.getAsJsonPrimitive("success")?.asBoolean == false) {
                        val code = jsonObject.getAsJsonPrimitive("code").asString
                        val message = jsonObject.getAsJsonPrimitive("message").asString
                        handleNetworkFailure(code, message, isUserDomain)
                    } // Succeed, ignored!
                } catch (t: Throwable) {
                    loge("Failed to handle network response", t)
                } finally {
                    response = response.newBuilder()
                        .body(json.toResponseBody(jsonType))
                        .build()
                }
            } else {
                handleNetworkFailure("${response.code}", response.message, false)
            }
        }
        return response
    }

    /** 处理网络错误 */
    private fun handleNetworkFailure(code: String?, message: String?, isUserDomain: Boolean) {
        if (code?.equals("${NetworkApi.CODE_UNAUTHORIZED}", ignoreCase = true) == true) {
            // 用户未授权/登录
            post(Event.EVENT_SESSION_EXPIRED)
        }
    }

    /** 是否是自己的服务器 */
    private fun isOwnHost(url: String): Boolean {
        val host = NetworkApi.getHost()
        return url.startsWith(host)
    }
}
