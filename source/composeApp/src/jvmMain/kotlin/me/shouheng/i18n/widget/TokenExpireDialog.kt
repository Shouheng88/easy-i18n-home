package me.shouheng.i18n.widget

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import easy_i18n.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

/** 会话过期对话框 */
@Composable
fun TokenExpireDialog(
    onLogin: () -> Unit,
    onClose: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onClose,
        title = { Text(stringResource(Res.string.user_session_expired)) },
        text = { Text(stringResource(Res.string.user_session_expired_tip)) },
        confirmButton = {
            TextButton(onClick = {
                onLogin()
                onClose()
            }) {
                Text(stringResource(Res.string.user_login))
            }
        },
        dismissButton = {
            TextButton(onClick = onClose) {
                Text(stringResource(Res.string.text_cancel))
            }
        }
    )
}