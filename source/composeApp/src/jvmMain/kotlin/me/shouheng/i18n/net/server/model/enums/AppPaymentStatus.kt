package me.shouheng.i18n.net.server.model.enums

import androidx.compose.ui.graphics.Color
import easy_i18n.composeapp.generated.resources.*
import me.shouheng.i18n.data.Colors
import org.jetbrains.compose.resources.StringResource

/**
 * @author [Shouheng.W](mailto:shouheng2015@gmail.com)
 * @version 1.0
 * @date 2020/11/9 10:03
 */
enum class AppPaymentStatus(
    val id: Int,
    val nameRes: StringResource,
    val color: Color,
) {
    // Committed just now, waiting for check
    COMMIT_SUCCEED(0,
        Res.string.premium_pay_status_commited,
        Colors.warn
    ),
    // Manually check passed
    CHECK_PASSED(1,
        Res.string.premium_pay_status_checked,
        Colors.warn
    ),
    // Manually check failed
    CHECK_FAILED(2,
        Res.string.premium_pay_status_check_failed,
        Colors.error
    ),
    // Took into effect
    DONE_SUCCEED(3,
        Res.string.premium_pay_status_succeed,
        Colors.success
    ),
    // Done failed
    DONE_FAILED(4,
        Res.string.premium_pay_status_failed,
        Colors.error
    );

    companion object {
        fun getTypeById(id: Int): AppPaymentStatus? = entries.firstOrNull { id == it.id }
    }
}