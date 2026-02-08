package me.shouheng.i18n.net.server.api

import me.shouheng.i18n.net.server.NetworkApi
import me.shouheng.i18n.net.server.model.base.BusinessRequest
import me.shouheng.i18n.net.server.model.base.BusinessResponse
import me.shouheng.i18n.net.server.model.so.AppRecommendSo
import me.shouheng.i18n.net.server.model.so.SocialAccountSo
import me.shouheng.i18n.net.server.model.vo.AppRecommendVo
import me.shouheng.i18n.net.server.model.vo.SocialAccount
import retrofit2.http.Body
import retrofit2.http.POST

val appServiceApi by lazy {
    NetworkApi.INSTANCE.getApi(AppService::class.java, NetworkApi.getHost())
}

/**
 * The App Controller
 *
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 2020/11/24 11:33
 */
interface AppService {

    /** 获取开发者的社交账号 */
    @POST("/rest/app/dev_accounts")
    suspend fun getDeveloperSocialAccountsAsync(
        @Body request: BusinessRequest<SocialAccountSo>
    ): BusinessResponse<List<SocialAccount>>

    /** 获取应用的社交账号 */
    @POST("/rest/app/app_accounts")
    suspend fun getAppSocialAccountsAsync(
        @Body request: BusinessRequest<SocialAccountSo>
    ): BusinessResponse<List<SocialAccount>>

    /**
     * Api: 推荐应用列表
     *
     * Desc:
     *
     * Parameter required:
     */
    @POST("/rest/app/recommend_apps")
    suspend fun getRecommendAppsV2Async(@Body request: BusinessRequest<AppRecommendSo>): BusinessResponse<List<AppRecommendVo>>
}
