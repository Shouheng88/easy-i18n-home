package me.shouheng.i18n.vm

import kotlinx.coroutines.flow.MutableStateFlow
import me.shouheng.i18n.data.common.Resource
import me.shouheng.i18n.manager.AppManger
import me.shouheng.i18n.manager.showError
import me.shouheng.i18n.net.server.model.vo.AppRecommendVo
import me.shouheng.i18n.net.server.model.vo.SocialAccount
import me.shouheng.i18n.net.server.repo.AppRepo

/** 应用信息相关的 ViewModel */
class InfoViewModel: BaseViewModel() {

    val developerAccounts = MutableStateFlow<List<SocialAccount>>(emptyList())
    val recommendApps = MutableStateFlow<List<AppRecommendVo>>(emptyList())
    val feedbackState = MutableStateFlow<Resource<Unit>?>(null)
    val showAppUpgradeDialog = MutableStateFlow(AppManger.shouldShowUpgradeDialog())
    val contacts = MutableStateFlow<List<SocialAccount>>(emptyList())

    /** 开发者社交账号 */
    fun getDeveloperAccounts() {
        AppRepo.getDeveloperAccounts {
            if (it.isSuccess && it.data != null) {
                developerAccounts.value = it.data
            } else {
                showError("${it.code}: ${it.message}")
            }
        }
    }

    /** 获取联系方式 */
    fun getAppAccounts() {
        AppRepo.getAppAccounts {
            if (it.isSuccess && it.data != null) {
                contacts.value = it.data
            } else {
                showError("${it.code}: ${it.message}")
            }
        }
    }

    /** 推荐应用列表 */
    fun getRecommendApps() {
        AppRepo.getRecommendApps {
            if (it.isSuccess && it.data != null) {
                recommendApps.value = it.data
            } else {
                showError("${it.code}: ${it.message}")
            }
        }
    }
}