package me.shouheng.i18n.net.server.interceptor

import me.shouheng.i18n.utils.isPossibleDebug
import okhttp3.logging.HttpLoggingInterceptor

/** Http 请求日志拦截器 */
class HttpLoggingInterceptorLogger : HttpLoggingInterceptor.Logger {

    override fun log(message: String) {
        if (isPossibleDebug()) {
            println("network: $message")
        }
    }
}