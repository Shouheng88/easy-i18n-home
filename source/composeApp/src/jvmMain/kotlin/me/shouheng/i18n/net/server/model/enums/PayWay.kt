package me.shouheng.i18n.net.server.model.enums

import androidx.compose.ui.graphics.Color
import easy_i18n.composeapp.generated.resources.*
import me.shouheng.i18n.data.Colors
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

/**
 * @author [Shouheng.W](mailto:shouheng2015@gmail.com)
 * @version 1.0
 * @date 2020/11/9 17:02
 */
enum class PayWay(
    val id: Int,
    val iconRes: DrawableResource,
    val nameRes: StringResource,
    val bkgColor: Color,
) {
    NONE(0, Res.drawable.ic_wechat, Res.string.premium_payway_none, Color.Unspecified),
    WX(1, Res.drawable.ic_wechat, Res.string.premium_payway_wechat, Colors.weChatColor),
    ALI_PAY(2, Res.drawable.ic_alipay, Res.string.premium_payway_alipay, Colors.aliPayColor),
    GOOGLE(3, Res.drawable.ic_alipay, Res.string.premium_payway_wechat, Color.Unspecified),
    APPLE(4, Res.drawable.ic_alipay, Res.string.premium_payway_wechat, Color.Unspecified),
    REDEEM(5, Res.drawable.ic_alipay, Res.string.premium_payway_wechat, Color.Unspecified)
    ;

    companion object {
        fun getTypeById(id: Int): PayWay? = entries.firstOrNull { id == it.id }
    }
}