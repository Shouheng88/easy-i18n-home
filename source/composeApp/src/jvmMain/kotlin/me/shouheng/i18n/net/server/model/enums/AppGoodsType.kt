package me.shouheng.i18n.net.server.model.enums

import androidx.compose.ui.graphics.Color
import easy_i18n.composeapp.generated.resources.*
import me.shouheng.i18n.data.Colors
import me.shouheng.i18n.data.Const
import me.shouheng.i18n.utils.ITextGetter
import me.shouheng.i18n.utils.TextConnector
import me.shouheng.i18n.utils.asGetter
import org.jetbrains.compose.resources.StringResource

/**
 * @author [Shouheng.W](mailto:shouheng2015@gmail.com)
 * @version 1.0
 * @date 2020/11/9 9:06
 */
enum class AppGoodsType(
    val id: Int,
    val color: Color,
    val nameRes: StringResource,
    val privileges: List<Privilege> = emptyList()
) {
    NORMAL(0x0000, Colors.premiumFreeColor, Res.string.premium_free_name, Privilege.FREE), // Normal
    VIP(0x0001, Colors.premiumColor, Res.string.premium_premium_name, listOf()),  // VIP
    SVIP(0x0002, Colors.professionalColor, Res.string.premium_professional_name, Privilege.PRO),  // SVIP
    SVIP_YEARLY(0x0009, Colors.professionalColor, Res.string.premium_professional_name, listOf()),  // SVIP YEARLY
    THREE_DAY_TRAIL_VIP(0x0003, Colors.premiumColor, Res.string.premium_premium_name),  // vip 3 days trail
    SEVEN_DAY_TRAIL_VIP(0x0004, Colors.premiumColor, Res.string.premium_7_free_trial_name),  // vip 7 days trail
    THREE_DAY_TRAIL_SVIP(0x0005, Colors.professionalColor, Res.string.premium_professional_name),  // svip 3 days trail
    SEVEN_DAY_TRAIL_SVIP(0x0006, Colors.professionalColor, Res.string.premium_7_free_trial_name),
    AUX_VIP(0x0007, Colors.premiumColor, Res.string.premium_premium_name), // aux vip
    AUX_SVIP(0x0008, Colors.professionalColor, Res.string.premium_professional_name);  // aux svip

    fun isVip(): Boolean = listOf(VIP, THREE_DAY_TRAIL_VIP, SEVEN_DAY_TRAIL_VIP, AUX_VIP).indexOf(this) >= 0

    fun isSVip(): Boolean = listOf(SVIP, THREE_DAY_TRAIL_SVIP, SEVEN_DAY_TRAIL_SVIP, AUX_SVIP, SVIP_YEARLY).indexOf(this) >= 0

    fun isAux(): Boolean = listOf(AUX_SVIP, AUX_VIP).indexOf(this) >= 0

    fun isFreeTrail(): Boolean = listOf(
        THREE_DAY_TRAIL_VIP,
        SEVEN_DAY_TRAIL_VIP,
        THREE_DAY_TRAIL_SVIP,
        SEVEN_DAY_TRAIL_SVIP
    ).indexOf(this) >= 0

    fun isNoneVip(): Boolean = NORMAL == this

    companion object {
        fun getTypeById(id: Int): AppGoodsType? = entries.firstOrNull { it.id == id }
    }
}

/** 权益 */
data class Privilege(
    val title: ITextGetter,
    val color: Color
) {
    companion object {
        val PRO = listOf<Privilege>(
            Privilege(TextConnector(
                Res.string.premium_privilege_project_count.asGetter(),
                " ".asGetter(),
                Res.string.text_unlimited.asGetter()
            ), Colors.success),
            Privilege(TextConnector(
                Res.string.premium_privilege_path_count.asGetter(),
                " ".asGetter(),
                Res.string.text_unlimited.asGetter()
            ), Colors.success),
            Privilege(Res.string.premium_privilege_translator.asGetter(), Colors.success)
        )
        val FREE = listOf<Privilege>(
            Privilege(TextConnector(
                Res.string.premium_privilege_project_count.asGetter(),
                " ".asGetter(),
                "${Const.MAX_PROJECT_COUNT_NONE_PREMIUM}".asGetter()
            ), Colors.warn),
            Privilege(TextConnector(
                Res.string.premium_privilege_path_count.asGetter(),
                " ".asGetter(),
                "${Const.MAX_FOLDER_COUNT_NONE_PREMIUM}".asGetter()
            ), Colors.warn),
            Privilege(Res.string.premium_privilege_translator_unable.asGetter(), Colors.error)
        )
    }
}
