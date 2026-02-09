package me.shouheng.i18n.net.server.repo

import com.google.gson.reflect.TypeToken
import me.shouheng.i18n.data.Const
import me.shouheng.i18n.data.common.Resource
import me.shouheng.i18n.data.common.onFailure
import me.shouheng.i18n.data.common.onSuccess
import me.shouheng.i18n.net.server.api.appServiceApi
import me.shouheng.i18n.net.server.callBusiness
import me.shouheng.i18n.net.server.model.base.BusinessRequest
import me.shouheng.i18n.net.server.model.so.*
import me.shouheng.i18n.net.server.model.vo.*
import me.shouheng.i18n.utils.*
import me.shouheng.i18n.utils.extension.md5

/** 应用 */
object AppRepo : BaseRepo() {

    private val keyAppRecommend by lazy { "__app_recommend__".md5("config") }
    private val keyLastReqRecommend by lazy { "__last_req_recommend__".md5("config") }
    private val keyLastReqDeveloper by lazy { "__last_req_developer__".md5("config") }
    private val keyLastReqApp by lazy { "__last_req_app__".md5("config") }

    private const val REQ_INTERVAL = 24 * 60 * 60 * 1000L

    private var recommends: List<AppRecommendVo>? = null

    private var developerAccounts: List<SocialAccount>? = null

    private var appAccounts: List<SocialAccount>? = null

    /** 推荐应用列表 */
    fun getRecommendApps(onResult: (Resource<List<AppRecommendVo>>) -> Unit) = execute(onResult) {
        if (recommends != null) {
            onResult(Resource.success(recommends!!))
            return@execute
        }
        val dataJson = settings.getStringOrNull(keyAppRecommend)
        var hasCache = false
        if (!dataJson.isNullOrEmpty()) {
            JSON.listFrom<AppRecommendVo>(dataJson, object : TypeToken<List<AppRecommendVo>>() {}.type)?.let {
                this@AppRepo.recommends = it
                onResult(Resource.success(it))
                hasCache = true
            }
        }

        val lastReqTime = settings.getLong(keyLastReqRecommend, 0L)
        if (hasCache && System.currentTimeMillis() - lastReqTime < REQ_INTERVAL) {
            return@execute
        }

        callBusiness {
            appServiceApi.getRecommendAppsV2Async(BusinessRequest.of(AppRecommendSo().apply {
                this.app = Const.APP_CODE
                this.channel = "web"
            }))
        }.onSuccess {
            it.data?.apply {
                val json = JSON.toJson(this)
                settings.putString(keyAppRecommend, json ?: "")
                settings.putLong(keyLastReqRecommend, System.currentTimeMillis())
                recommends = this
                onResult(Resource.success(this))
            }
        }.onFailure {
            onResult(it)
        }
    }

    /** 获取开发者的社交账号 */
    fun getDeveloperAccounts(onResult: (Resource<List<SocialAccount>>) -> Unit) = execute(onResult) {
        // 从内存缓存中读取
        if (null != developerAccounts) {
            onResult(Resource.success(developerAccounts))
            return@execute
        }

        // 从磁盘缓存中读取
        val cache = settings.getString("_developer_accounts_cache_", "")
        var hasCache = false
        if (!cache.isEmpty()) {
            this@AppRepo.developerAccounts = JSON.listFrom(cache, object : TypeToken<List<SocialAccount>>() {}.type)
            if (null != developerAccounts) {
                onResult(Resource.success(developerAccounts))
                hasCache = true
            }
        }

        val lastReqTime = settings.getLong(keyLastReqDeveloper, 0L)
        if (hasCache && System.currentTimeMillis() - lastReqTime < REQ_INTERVAL) {
            return@execute
        }

        // 从网络中读取
        callBusiness {
            appServiceApi.getDeveloperSocialAccountsAsync(BusinessRequest.of(SocialAccountSo().apply {
                this.oversea = !DeviceUtils.isChineseMainland()
            }))
        }.onSuccess {
            // 内存缓存数据
            developerAccounts = it.data
            // 磁盘缓存数据
            if (developerAccounts != null) {
                val json = JSON.toJson(developerAccounts!!)
                settings.putString("_developer_accounts_cache_", json ?: "")
                settings.putLong(keyLastReqDeveloper, System.currentTimeMillis())
            }
            // 回调
            onResult(Resource.success(developerAccounts))
        }.onFailure {
            onResult(it)
        }
    }

    /** 获取应用的社交账号 */
    fun getAppAccounts(onResult: (Resource<List<SocialAccount>>) -> Unit) = execute(onResult) {
        // 从内存缓存中读取
        if (null != appAccounts) {
            onResult(Resource.success(appAccounts))
            return@execute
        }

        // 从磁盘缓存中读取
        val cache = settings.getString("__app_accounts_cache__", "")
        var hasCache = false
        if (!cache.isEmpty()) {
            this@AppRepo.appAccounts = JSON.listFrom(cache, object : TypeToken<List<SocialAccount>>() {}.type)
            if (null != appAccounts) {
                onResult(Resource.success(appAccounts))
                hasCache = true
            }
        }

        val lastReqTime = settings.getLong(keyLastReqApp, 0L)
        if (hasCache && System.currentTimeMillis() - lastReqTime < REQ_INTERVAL) {
            return@execute
        }

        // 从网络中读取
        callBusiness {
            appServiceApi.getAppSocialAccountsAsync(BusinessRequest.of(SocialAccountSo().apply {
                this.oversea = !DeviceUtils.isChineseMainland()
                this.app = Const.APP_CODE
            }))
        }.onSuccess {
            // 内存缓存数据
            appAccounts = it.data
            // 磁盘缓存数据
            if (appAccounts != null) {
                val json = JSON.toJson(appAccounts!!)
                settings.putString("__app_accounts_cache__", json ?: "")
                settings.putLong(keyLastReqApp, System.currentTimeMillis())
            }
            // 回调
            onResult(Resource.success(appAccounts))
        }.onFailure {
            onResult(it)
        }
    }
}
