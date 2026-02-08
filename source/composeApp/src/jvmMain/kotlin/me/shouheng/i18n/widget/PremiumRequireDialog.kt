package me.shouheng.i18n.widget

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import easy_i18n.composeapp.generated.resources.*
import me.shouheng.i18n.data.Colors
import me.shouheng.i18n.data.Event
import me.shouheng.i18n.data.Router.ROUTE_PREMIUM
import me.shouheng.i18n.manager.post
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/** 会员对话框 */
@Composable
fun PremiumRequireDialog(
    message: String = stringResource(Res.string.premium_require_message),
    onClose: () -> Unit
) {
    Dialog(onDismissRequest = onClose) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(bottom = 20.dp, top = 20.dp)
                    .fillMaxWidth(0.9f),
                verticalArrangement = Arrangement.spacedBy(30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 15.dp)
                ) {
                    Spacer(Modifier.weight(1f))
                    Icon(
                        Icons.Default.Cancel,
                        contentDescription = null,
                        tint = Colors.premiumForegroundColor,
                        modifier = Modifier.clickable {
                            onClose()
                        }
                    )
                }
                Icon(
                    painter = painterResource(Res.drawable.ic_diamond),
                    contentDescription = null,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Colors.premiumBackgroundColor)
                        .size(100.dp)
                        .padding(15.dp),
                    tint = Colors.premiumForegroundColor
                )
                Column(
                    modifier = Modifier,
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        stringResource(Res.string.premium_require_title),
                        style = TextStyle(fontSize = 18.sp),
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        message,
                        fontSize = 15.sp,
                        modifier = Modifier.padding(horizontal = 15.dp),
                        textAlign = TextAlign.Center
                    )
                }
                Text(
                    stringResource(Res.string.premium_require_button),
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .clip(RoundedCornerShape(30.dp))
                        .background(Colors.premiumForegroundColor)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(bounded = true),
                            onClick = {
                                post(Event.EVENT_NAME_TO_PAGE, ROUTE_PREMIUM)
                                onClose()
                            }
                        )
                        .padding(vertical = 12.dp, horizontal = 15.dp),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview
@Composable
fun PremiumRequireDialogPreview() {
    PremiumRequireDialog {  }
}