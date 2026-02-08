package me.shouheng.i18n.net.server.model.enums

import easy_i18n.composeapp.generated.resources.Res
import easy_i18n.composeapp.generated.resources.feedback_type_bug
import easy_i18n.composeapp.generated.resources.feedback_type_improve
import org.jetbrains.compose.resources.StringResource

/**
 * 反馈的类型
 *
 * @author [WngShhng](mailto:shouheng2015@gmail.com)
 * @date 2019/11/5 22:16
 */
enum class FeedbackType(
    private val id: Int,
    val nameRes: StringResource
) {
    // BUG 或者 Crash 类型
    BUG(0, Res.string.feedback_type_bug),
    // 功能建议类型
    IMPROVEMENT(1, Res.string.feedback_type_improve);

    companion object {
        fun getTypeById(id: Int): FeedbackType? = entries.firstOrNull { id == it.id }
    }
}
