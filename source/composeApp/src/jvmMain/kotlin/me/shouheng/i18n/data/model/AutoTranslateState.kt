package me.shouheng.i18n.data.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import easy_i18n.composeapp.generated.resources.Res
import easy_i18n.composeapp.generated.resources.translate_state_completed
import easy_i18n.composeapp.generated.resources.translate_state_error
import easy_i18n.composeapp.generated.resources.translate_state_translating
import me.shouheng.i18n.data.Colors
import org.jetbrains.compose.resources.stringResource

/** 自动翻译状态 */
data class AutoTranslateState(
    var status: Status,
    /** 待翻译的【词条】总量 */
    var total: Int = 0,
    /** 翻译的【词条】总量 */
    var translatedCount: Int = 0,
    /** 待翻译的次数 */
    var errorCount: Int = 0,
    /** 跳过的【词条】的数量 */
    var skippedCount: Int = 0,
    /** 服务器请求的数量 */
    var requestCount: Int = 0,
    /** 是否应该刷新单词列表 */
    var canReload: Boolean = false,
    /** 错误码 */
    var errorCode: String? = null,
    /** 错误信息 */
    var errorMessage: String? = null,
) {

    enum class Status {
        RUNNING,
        ERROR,
        COMPLETED;

        @Composable
        fun getDisplayName(): String = when(this) {
            RUNNING -> stringResource(Res.string.translate_state_translating)
            ERROR -> stringResource(Res.string.translate_state_error)
            COMPLETED -> stringResource(Res.string.translate_state_completed)
        }

        fun getDisplayColor(): Color = when(this) {
            RUNNING -> Colors.warn
            ERROR -> Colors.error
            COMPLETED -> Colors.success
        }
    }

    fun isRunning(): Boolean = this.status != Status.ERROR && this.status != Status.COMPLETED

    fun getPercentageText(): String = if (total == 0) "100%" else "${translatedCount * 100 / total}%"

    override fun toString(): String {
        return "AutoTranslateState(" +
                "status=$status, " +
                "total=$total, " +
                "translatedCount=$translatedCount, " +
                "errorCount=$errorCount, " +
                "skippedCount=$skippedCount, " +
                "requestCount=$requestCount)"
    }
}