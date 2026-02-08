package me.shouheng.i18n.net.server.model.vo

import easy_i18n.composeapp.generated.resources.Res
import easy_i18n.composeapp.generated.resources.premium_free_name
import me.shouheng.i18n.data.Const
import me.shouheng.i18n.net.server.model.base.AbstractVo
import me.shouheng.i18n.net.server.model.enums.AppGoodsType
import org.jetbrains.compose.resources.getString

/**
 * AppGoodsVo
 *
 * @author [Shouheng.W](mailto:shouheng2015@gmail.com)
 * @version 1.0
 * @date 2020/11/09 10:15
 */
class AppGoodsVo : AbstractVo() {
    var appId: Long? = null
    var appName: String? = null
    var price: Int? = null
    var priceType: String? = null
    var name: String? = null
    var brief: String? = null
    var type: AppGoodsType? = null
    var onSale: Boolean? = null
    var discounts: List<AppGoodsDiscountVo>? = null
    var googleGoodsId: String? = null
    /** freeTrial 被服务器占用，无法无法使用 */
    var freeTrialProduct: AppGoodsVo? = null

    /** 按照 5 年算，每天的使用成本 */
    val dailyCostFor5Years: Float
        get() = (price ?: 0) * 1f / 100 / 365 / 5

    companion object {

        private var FREE: AppGoodsVo? = null

        suspend fun getFreeVersion(): AppGoodsVo {
            if (FREE == null) {
                FREE = AppGoodsVo().apply {
                    this.appId = Const.APP_ID
                    this.price = 0
                    this.priceType = ""
                    this.name = getString(Res.string.premium_free_name)
                    this.type = AppGoodsType.NORMAL
                }
            }
            return FREE!!
        }
    }
}