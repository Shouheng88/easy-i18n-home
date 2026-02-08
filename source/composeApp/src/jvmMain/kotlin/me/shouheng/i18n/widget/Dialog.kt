package me.shouheng.i18n.widget

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.shouheng.i18n.data.UIConst.DIALOG_BOTTOM_PADDING

@Preview
@Composable
fun NormalDialogTitle(title: String) {
    Text(
        title,
        style = TextStyle(fontSize = 22.sp),
        textAlign = TextAlign.Start,
        modifier = Modifier
            .padding(vertical = 20.dp, horizontal = 15.dp)
            .fillMaxWidth(),
    )
}

/** 底部按钮 */
@Composable
fun NormalDialogFooter(
    left: String? = null,
    right: String? = null,
    onLeft: () -> Unit = {},
    onRight: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .padding(vertical = DIALOG_BOTTOM_PADDING.dp, horizontal = 15.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Spacer(Modifier.weight(1f))
        if (left != null) {
            Button(onClick = onLeft, colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.primary
            ),) {
                Text(left, fontSize = 14.sp)
            }
        }
        if (right != null) {
            Button(onClick = onRight) {
                Text(right, fontSize = 14.sp)
            }
        }
    }
}