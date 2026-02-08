package me.shouheng.i18n.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Switch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Light
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SettingSectionTitle(title: String) {
    Text(
        title,
        fontSize = 12.sp,
        modifier = Modifier.padding(horizontal = 10.dp),
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
fun SettingSectionCard(
    title: String? = null,
    modifier: Modifier = Modifier.fillMaxWidth(),
    items: @Composable () -> Unit
) {
    if (title == null) {
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(10.dp),
            modifier = modifier
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            items()
            Spacer(modifier = Modifier.height(10.dp))
        }
    } else {
        Column(
            modifier = modifier
        ) {
            SettingSectionTitle(title)
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(10.dp))
                items()
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
fun SettingSwitchItemView(
    imageVector: ImageVector? = null,
    title: String? = null,
    summary: String? = null,
    enabled: Boolean,
    onChanged: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .clickable(onClick = {
                onChanged(!enabled)
            })
            .heightIn(min = 40.dp)
            .padding(end = 10.dp, start = 15.dp)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        if (imageVector != null) {
            Icon(imageVector, contentDescription = null)
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            horizontalAlignment = Alignment.Start
        ) {
            if (title != null) {
                Text(
                    title,
                    fontSize = 14.sp,
                    modifier = Modifier,
                    lineHeight = 18.sp
                )
            }
            if (summary != null) {
                Text(
                    summary,
                    fontSize = 12.sp,
                    modifier = Modifier.alpha(0.8f),
                    lineHeight = 16.sp
                )
            }
        }
        Switch(enabled, onChanged)
    }
}

@Composable
fun SettingItemView(
    imageVector: ImageVector? = null,
    title: String? = null,
    summary: String? = null,
    foot: String? = null,
    showArrow: Boolean = true,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .heightIn(min = 40.dp)
            .padding(end = 10.dp, start = 15.dp)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        if (imageVector != null) {
            Icon(imageVector, contentDescription = null)
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            horizontalAlignment = Alignment.Start
        ) {
            if (title != null) {
                Text(
                    title,
                    fontSize = 14.sp,
                    modifier = Modifier,
                    lineHeight = 18.sp
                )
            }
            if (summary != null) {
                Text(
                    summary,
                    fontSize = 12.sp,
                    modifier = Modifier.alpha(0.8f),
                    lineHeight = 16.sp
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (foot != null) {
                Text(foot, fontSize = 12.sp, modifier = Modifier.alpha(0.8f))
            }
            if (showArrow) {
                Icon(
                    Icons.Default.ChevronRight,
                    contentDescription = null,
                    modifier = Modifier
                        .alpha(0.8f)
                        .size(18.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun SettingItemsPreview() {
    var enabled by remember { mutableStateOf(false) }
    SettingSectionTitle("title")
    SettingSwitchItemView(
        Icons.Default.Light,
        "Title",
        "Sub Title",
        enabled = enabled,
        onChanged = {
            enabled = it
        }
    )
}