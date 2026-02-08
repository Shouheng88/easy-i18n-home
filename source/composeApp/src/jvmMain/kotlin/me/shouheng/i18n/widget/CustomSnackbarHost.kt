package me.shouheng.i18n.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.shouheng.i18n.data.common.SnackMessage

@Composable
fun CustomSnackbarHost(snackBarHostState: SnackbarHostState) {
    SnackbarHost(snackBarHostState) { data ->
        val type = SnackMessage.Type.from(data.visuals.actionLabel)
        Row(
            Modifier
                .padding(12.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(type.backgroundColor)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(type.icon, contentDescription = null, tint = Color.White)
            Text(
                text = data.visuals.message,
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}

suspend fun SnackbarHostState.showMessage(snackMessage: SnackMessage?) {
    if (snackMessage != null) {
        this.showSnackbar(
            snackMessage.content,
            actionLabel = snackMessage.type.name,
            duration = SnackbarDuration.Short
        )
    }
}