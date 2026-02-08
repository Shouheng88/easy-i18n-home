package me.shouheng.i18n.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import me.shouheng.i18n.data.UIConst

@Composable
fun SimpleIconButton(
    icon: ImageVector,
    tint: Color = MaterialTheme.colorScheme.primary,
    background: Color = MaterialTheme.colorScheme.secondaryContainer,
    contentDescription: String? = null,
    onClick: () -> Unit = {  }
) {
    Icon(
        icon,
        contentDescription = contentDescription,
        modifier = Modifier
            .size(UIConst.ICON_BUTTON_SIZE.dp, UIConst.ICON_BUTTON_SIZE.dp)
            .clip(CircleShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true, radius = UIConst.ICON_BUTTON_SIZE.dp),
                onClick = onClick
            )
            .background(background)
            .padding(UIConst.ICON_BUTTON_PADDING.dp),
        tint = tint
    )
}

@Composable
fun SimpleIconButton(
    icon: Painter,
    tint: Color = MaterialTheme.colorScheme.primary,
    background: Color = MaterialTheme.colorScheme.secondaryContainer,
    contentDescription: String? = null,
    onClick: () -> Unit = {  }
) {
    Icon(
        icon,
        contentDescription = contentDescription,
        modifier = Modifier
            .size(UIConst.ICON_BUTTON_SIZE.dp, UIConst.ICON_BUTTON_SIZE.dp)
            .clip(CircleShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true, radius = UIConst.ICON_BUTTON_SIZE.dp),
                onClick = onClick
            )
            .background(background)
            .padding(UIConst.ICON_BUTTON_PADDING.dp),
        tint = tint
    )
}