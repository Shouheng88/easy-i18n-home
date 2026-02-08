package me.shouheng.i18n.net.server

import me.shouheng.i18n.net.server.interceptor.HttpLoggingInterceptorLogger
import me.shouheng.i18n.net.server.interceptor.ServerFailureInterceptor
import me.shouheng.i18n.utils.JSON
import me.shouheng.i18n.utils.extension.loge
import me.shouheng.i18n.utils.extension.md5
import me.shouheng.i18n.utils.settings
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

/** Network api. */
class NetworkApi private constructor(): AbsNetworkApi() {

    companion object {

        private const val TIMEOUT_SECONDS = 10L

        private val onHostChangeListeners = mutableListOf<OnHostChangeListener>()

        /** 全部可用的 Host 信息 */
        private val hosts = mutableListOf<String>()

        /** 当前使用中的 Host */
        private var curHost: String? = null

        const val CODE_IO_ERROR = -2
        const val CODE_NO_INTERNET = -1
        const val CODE_SUCCESS = 200
        const val CODE_UNAUTHORIZED = 401

        val INSTANCE: NetworkApi by lazy(
            mode = LazyThreadSafetyMode.SYNCHRONIZED
        ) { NetworkApi() }

        /** 获取当前的 Host */
        @JvmStatic
        fun getHost(): String {
            if (curHost != null) {
                return curHost!!
            }
            // 从 KV 中获取上次使用的 Host：该 Host 存在且可用的时候才使用它
            val key = "__host__".md5("host")
            val host = settings.getStringOrNull(key)
            if (!host.isNullOrEmpty() && hosts.contains(host)) {
                this.curHost = host
                return curHost!!
            }
            settings.remove(key)
            // 使用可用服务器中的第一个
            this.curHost = this.hosts.firstOrNull()
            if (this.curHost == null) {
                throw IllegalStateException("available host not found!")
            }
            return this.curHost!!
        }

        /** 设置可用的服务器列表 */
        @JvmStatic
        fun setHosts(hosts: List<String>) {
            this.hosts.clear()
            this.hosts.addAll(hosts)
        }

        /** 切换到指定的 Host */
        @JvmStatic
        fun switchHost(host: String) {
            if (hosts.contains(host)) {
                this.curHost = host
                val key = "__host__".md5("host")
                settings.putString(key, host)
                notifyHostChanged(host)
            } else {
                loge("unable to switch to an unavailable host")
            }
        }

        /** 指定的请求是否为用户领域的请求 */
        @JvmStatic
        fun isUserDomain(url: String): Boolean {
            val host = getHost()
            return url.startsWith("${host}rest/user")
        }

        /** 是否是自己的服务器 */
        fun isOwnHost(url: String): Boolean {
            val host = getHost()
            return url.startsWith(host)
        }

        /** 注册 Host 改变监听事件 */
        fun registerOnHostChangeListener(listener: OnHostChangeListener) {
            if (!onHostChangeListeners.contains(listener)) {
                onHostChangeListeners.add(listener)
            }
        }

        /** 取消注册 Host 改变监听事件 */
        fun unRegisterOnHostChangeListener(listener: OnHostChangeListener) {
            if (onHostChangeListeners.contains(listener)) {
                onHostChangeListeners.remove(listener)
            }
        }

        private fun notifyHostChanged(host: String) {
            onHostChangeListeners.forEach { it.onHostChanged(host) }
        }

        /** Host 改变事件监听 */
        interface OnHostChangeListener {

            fun onHostChanged(host: String)
        }
    }

    override fun initRetrofit(baseUrl: String, builder: Retrofit.Builder) {
        builder.addConverterFactory(JSON.getConvertFactory())
    }

    override fun initOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .addInterceptor(getHttpLoggingInterceptor())
            .addInterceptor(ServerFailureInterceptor())
            .build()
    }

    /** Get the http logging interceptor. */
    private fun getHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor(HttpLoggingInterceptorLogger()).apply {
            setLevel(HttpLoggingInterceptor.Level.HEADERS)
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }
}